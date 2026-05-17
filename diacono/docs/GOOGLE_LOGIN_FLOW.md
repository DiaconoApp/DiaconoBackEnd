# Mapeamento do Fluxo de Login com Google

## Objetivo
Este documento mapeia o fluxo do login com Google no backend `diacono`, usando o `PROJECT_ANALYSIS.md` como guia de arquitetura para localizar as camadas envolvidas.

Pelo recorte arquitetural do projeto, o fluxo atravessa:
- `infrastructure/controllers`: entrada HTTP
- `usecases/googleauth`: regra de autenticacao Google
- `infrastructure/auth`: validacao tecnica do `idToken`
- `usecases/membro`: localizacao do membro interno
- `usecases`: geracao do JWT proprio da API
- `infrastructure/persistence`: persistencia opcional do `refreshToken`

## Endpoint de entrada
- Rota: `POST /api/v1/auth/google`
- Controller: `GoogleAuthController`
- Request body: `GoogleAuthRequestDTO`

Payload esperado:

```json
{
  "idToken": "token-id-do-google",
  "refreshToken": "refresh-token-opcional"
}
```

Observacao:
- `idToken` e obrigatorio para autenticar o usuario junto ao Google.
- `refreshToken` e opcional e, quando enviado, e armazenado pela aplicacao.

## Visao geral do fluxo
```text
Cliente
  -> POST /api/v1/auth/google
  -> GoogleAuthController
  -> LoginGoogleUseCase
  -> AutenticarGoogleUseCase
  -> GoogleIdTokenVerifierImpl
  -> Google OIDC Decoder
  -> BuscarPorEmaiUseCase
  -> AtualizarSecretGoogleUseCase (opcional)
  -> GenerateTokenUseCase
  -> Response 200 com JWT da API
```

## Sequencia detalhada
### 1. Recepcao da requisicao
O cliente chama `POST /api/v1/auth/google` com um `idToken` emitido pelo Google e, opcionalmente, um `refreshToken`.

O `GoogleAuthController` nao faz regra de negocio. Ele apenas delega o payload para `LoginGoogleUseCase.execute(...)`.

### 2. Validacao do token Google
`LoginGoogleUseCase` chama `AutenticarGoogleUseCase.execute(idToken)`.

Esse use case delega a verificacao tecnica do token para `GoogleIdTokenVerifierImpl`, que:
- instancia um `JwtDecoder` com issuer `https://accounts.google.com`
- executa `decode(idToken)`
- extrai do token:
  - `audience`
  - `email`
  - `email_verified`

Se o token nao puder ser decodificado ou validado pelo decoder, a implementacao lanca:
- `BadCredentialsException("Token do Google invalido")`

### 3. Validacoes de regra sobre os claims
Depois da decodificacao, `AutenticarGoogleUseCase` aplica duas validacoes de negocio:

#### 3.1. Audience do token
O backend le `google.oauth.clientId` via `GoogleOAuthProperties`.

As regras sao:
- se `clientId` nao estiver configurado, falha
- se a `audience` do token nao contiver esse `clientId`, falha

Erros possiveis:
- `BadCredentialsException("Configuracao do Google OAuth ausente na aplicacao")`
- `BadCredentialsException("Token do Google nao pertence a aplicacao")`

#### 3.2. Email verificado
O backend exige:
- `email` preenchido
- `email_verified == true`

Erro possivel:
- `BadCredentialsException("Email do Google nao verificado")`

Se essas validacoes passam, o resultado e um `GoogleIdTokenDTO` confiavel para uso interno.

### 4. Vinculo do usuario Google com um membro interno
Com o email extraido do token, `LoginGoogleUseCase` chama `BuscarPorEmaiUseCase.execute(email)`.

Esse ponto e central no fluxo: o login Google nao cria usuario automaticamente. O email recebido do Google precisa corresponder a um `Membro` ja cadastrado no sistema.

Comportamento real da implementacao:
- `BuscarPorEmaiUseCase` consulta `MembroRepository.findByEmail(email)`
- se nao encontrar, lanca `ObjectNotFoundException("Membro não encontrado com o email fornecido.")`

Implicacao:
- o login Google depende de pre-cadastro do membro no banco
- na ausencia desse vinculo, a resposta nao e `401`, e sim `404`

Observacao tecnica importante:
- `LoginGoogleUseCase` ainda possui um `if (membro == null)` com `BadCredentialsException("Usuario nao cadastrado")`
- esse ramo esta desalinhado com o comportamento atual de `BuscarPorEmaiUseCase`, porque o use case de busca lanca excecao antes de retornar `null`
- os testes de `LoginGoogleUseCase` cobrem o caso `null`, mas esse cenario nao representa o caminho real da implementacao em producao

### 5. Persistencia opcional do refresh token Google
Se `refreshToken` vier no request, `LoginGoogleUseCase` chama `AtualizarSecretGoogleUseCase.execute(...)`.

Dados persistidos em `GoogleRefreshTokenMembro`:
- `membroId`
- `email`
- `refreshToken`

Esse salvamento usa `GoogleRefreshTokenMembroRepository`, implementado por `GoogleRefreshTokenMembroRepositoryImpl`, que delega ao `GoogleRefreshTokenMembroJpaRepository`.

Observacoes:
- o refresh token nao participa da autenticacao principal do endpoint
- ele apenas e armazenado quando presente
- nao existe, neste fluxo, leitura do refresh token para renovar sessao

### 6. Geracao do JWT interno da API
Com o `Membro` localizado, `LoginGoogleUseCase` chama `GenerateTokenUseCase.execute(membro)`.

Esse use case gera o token proprio da aplicacao com `JwtEncoder` e inclui claims como:
- `issuer = "diacono-api"`
- `subject = membro.idExterno`
- `scope = cargo do membro`
- `nome`
- `idade`
- `fk_igreja`
- `igreja`

O tempo de expiracao vem de:
- `app.jwt.expiration-seconds`
- default: `3600`

### 7. Resposta HTTP
No caminho de sucesso, o endpoint retorna `200 OK` com `LoginResponseDTO`:

```json
{
  "acessToken": "jwt-da-api",
  "expiresIn": 3600
}
```

Observacao:
- o campo foi nomeado no codigo como `acessToken`

## Regras de negocio consolidadas
1. O cliente deve enviar um `idToken` valido emitido pelo Google.
2. O token precisa ter audience compativel com o `google.oauth.clientId` da aplicacao.
3. O email dentro do token precisa estar verificado no Google.
4. O email do token precisa corresponder a um `Membro` ja existente no sistema.
5. O backend nao cria conta automaticamente no login Google.
6. O `refreshToken` so e persistido se vier no request.
7. O retorno final do endpoint e sempre um JWT proprio do backend, nao o token do Google.

## Mapa por classe
### Entrada HTTP
- `infrastructure/controllers/GoogleAuthController`

### DTOs
- `applications/dtos/googleauth/GoogleAuthRequestDTO`
- `applications/dtos/googleauth/GoogleIdTokenDTO`
- `applications/dtos/login/LoginResponseDTO`

### Regras de autenticacao Google
- `usecases/googleauth/LoginGoogleUseCase`
- `usecases/googleauth/AutenticarGoogleUseCase`
- `usecases/googleauth/AtualizarSecretGoogleUseCase`

### Integracao tecnica com Google
- `infrastructure/auth/GoogleIdTokenVerifier`
- `infrastructure/auth/GoogleIdTokenVerifierImpl`
- `global/config/GoogleOAuthProperties`

### Cadastro interno e token da API
- `usecases/membro/BuscarPorEmaiUseCase`
- `usecases/GenerateTokenUseCase`

### Persistencia de refresh token
- `domain/entity/GoogleRefreshTokenMembro`
- `domain/repository/GoogleRefreshTokenMembroRepository`
- `infrastructure/persistence/gateway/GoogleRefreshTokenMembroRepositoryImpl`
- `infrastructure/persistence/springdata/GoogleRefreshTokenMembroJpaRepository`

### Tratamento de erros
- `global/error/RestExceptionHandler`
- `global/error/exceptions/BadCredentialsException`
- `global/error/exceptions/ObjectNotFoundException`

## Possiveis respostas de erro no fluxo
### 401 Unauthorized
Quando ocorre falha de autenticacao ou validacao do token Google:
- `Token do Google invalido`
- `Configuracao do Google OAuth ausente na aplicacao`
- `Token do Google nao pertence a aplicacao`
- `Email do Google nao verificado`

### 404 Not Found
Quando o email vindo do Google nao encontra um membro interno:
- `Membro não encontrado com o email fornecido.`

## Diagrama resumido de decisao
```text
POST /api/v1/auth/google
  -> idToken valido?
     -> nao: 401
     -> sim:
        -> audience confere com google.oauth.clientId?
           -> nao: 401
           -> sim:
              -> email esta verificado?
                 -> nao: 401
                 -> sim:
                    -> existe Membro com esse email?
                       -> nao: 404
                       -> sim:
                          -> refreshToken foi enviado?
                             -> sim: salva refresh token
                             -> nao: segue sem persistir
                          -> gera JWT interno
                          -> retorna 200
```

## Conclusao
O login com Google neste backend nao usa sessao OAuth2 completa no servidor. O cliente autentica no Google, envia o `idToken` para a API e o backend:
- valida o token recebido
- garante que o email pertence a um membro interno existente
- persiste o `refreshToken` se ele vier no payload
- emite um JWT proprio para autorizacao nas demais rotas da aplicacao

Em termos de arquitetura, o fluxo esta coerente com o desenho descrito no `PROJECT_ANALYSIS.md`: controller fino na camada de infraestrutura, regra centralizada em use cases e persistencia isolada por repositores/gateways.
