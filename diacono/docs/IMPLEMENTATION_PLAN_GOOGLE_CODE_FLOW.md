# Plano de Implementacao do Novo Fluxo de Login Google por Authorization Code

## Objetivo
Este documento descreve como migrar o fluxo atual de login Google, baseado em `idToken` recebido do frontend, para um novo fluxo em que o backend recebe um `authorization code`, troca esse codigo no Google por tokens e, a partir disso, conclui o login interno.

O objetivo principal da mudanca e permitir que o backend passe a controlar a obtencao do `refresh_token`, em vez de depender do frontend para repassa-lo.

## Base de comparacao
Fluxo atual documentado em:
- `diacono/docs/GOOGLE_LOGIN_FLOW.md`

Classes principais do fluxo atual:
- `GoogleAuthController`
- `GoogleAuthRequestDTO`
- `LoginGoogleUseCase`
- `AutenticarGoogleUseCase`
- `GoogleIdTokenVerifierImpl`
- `AtualizarSecretGoogleUseCase`
- `GenerateTokenUseCase`

## Comparacao entre os fluxos
### Fluxo atual
No fluxo atual:
1. O frontend autentica o usuario no Google.
2. O frontend envia para o backend:
   - `idToken`
   - `refreshToken` opcional
3. O backend valida o `idToken`.
4. O backend extrai o email.
5. O backend busca o `Membro` pelo email.
6. O backend salva o `refreshToken`, se ele vier no payload.
7. O backend gera o JWT proprio da API.

Caracteristica principal:
- o backend depende de tokens ja obtidos fora dele

### Novo fluxo proposto
No novo fluxo:
1. O frontend autentica o usuario no Google.
2. O Google devolve um `authorization code`.
3. O frontend envia esse `authorization code` ao backend.
4. O backend troca o `authorization code` no Google por tokens.
5. O backend recebe pelo menos:
   - `id_token`
   - `access_token`
   - `refresh_token` quando o Google o emitir
6. O backend valida o `id_token`.
7. O backend extrai o email.
8. O backend busca o `Membro` pelo email.
9. O backend salva o `refresh_token`, quando ele vier.
10. O backend gera o JWT proprio da API.

Caracteristica principal:
- o backend passa a controlar a obtencao do `refresh_token`

## Diferencas estruturais
### O que muda no frontend
O frontend deixa de enviar:
- `idToken`
- `refreshToken`

O frontend passa a enviar:
- `authorizationCode`
- `redirectUri`

Opcionalmente, caso o fluxo use PKCE:
- `codeVerifier`

Payload esperado no novo desenho:

```json
{
  "authorizationCode": "code_recebido_do_google",
  "redirectUri": "https://app.exemplo.com/auth/google/callback",
  "codeVerifier": "valor-opcional-se-pkce-for-usado"
}
```

Observacoes:
- `redirectUri` deve ser o mesmo valor usado no fluxo iniciado no frontend e o mesmo configurado no Google Cloud Console.
- `codeVerifier` so deve existir se o fluxo do frontend estiver usando PKCE.

### O que muda no backend
O backend deixa de:
- confiar em `idToken` vindo diretamente do frontend como entrada principal do login
- depender do frontend para receber `refreshToken`

O backend passa a:
- receber um `authorization code`
- chamar o token endpoint do Google
- obter o `id_token` e o `refresh_token` diretamente do Google
- seguir com a validacao do `id_token` e a geracao do JWT interno

## Novo fluxo detalhado
```text
Frontend
  -> autentica usuario no Google
  -> recebe authorization code
  -> POST /api/v1/auth/google
       body: authorizationCode, redirectUri, codeVerifier?

Backend
  -> GoogleAuthController
  -> LoginGoogleUseCase
  -> GoogleAuthorizationCodeExchanger
  -> Token endpoint do Google
  -> recebe id_token / access_token / refresh_token?
  -> AutenticarGoogleUseCase
  -> BuscarPorEmaiUseCase
  -> AtualizarSecretGoogleUseCase (se refresh_token vier)
  -> GenerateTokenUseCase
  -> Response 200 com JWT da API
```

## Plano de implementacao
### 1. Alterar o contrato recebido do frontend
#### Situacao atual
Hoje o DTO de entrada e `GoogleAuthRequestDTO` e recebe:
- `idToken`
- `refreshToken`

#### Mudanca proposta
Substituir esse contrato por um DTO voltado a `authorization code`.

Opcoes de nomenclatura:
- `GoogleAuthorizationCodeRequestDTO`
- `GoogleOAuthCodeRequestDTO`

Campos sugeridos:
- `authorizationCode`
- `redirectUri`
- `codeVerifier` opcional

Impacto no codigo:
- alterar ou substituir `applications/dtos/googleauth/GoogleAuthRequestDTO`
- ajustar `GoogleAuthController` para consumir o novo DTO

### 2. Criar DTOs para a resposta do Google
O backend precisara modelar a resposta do token endpoint do Google.

DTO sugerido:
- `GoogleTokenResponseDTO`

Campos esperados:
- `accessToken`
- `idToken`
- `refreshToken`
- `expiresIn`
- `tokenType`
- `scope`

Observacao:
- `refreshToken` pode nao vir em todas as respostas

### 3. Criar uma abstracao para trocar o authorization code no Google
Hoje existe apenas a validacao de `idToken` em `GoogleIdTokenVerifierImpl`.

Sera necessario criar uma nova integracao para:
- chamar o endpoint de token do Google
- enviar os parametros da troca de `authorization code`
- receber a resposta do Google

Sugestao de desenho:
- interface: `GoogleAuthorizationCodeExchanger`
- implementacao: `GoogleAuthorizationCodeExchangerImpl`

Responsabilidades dessa integracao:
- montar a requisicao HTTP
- enviar:
  - `code`
  - `client_id`
  - `client_secret`
  - `redirect_uri`
  - `grant_type=authorization_code`
  - `code_verifier`, se aplicavel
- tratar erros do Google
- devolver um DTO interno com os tokens

### 4. Reorganizar o LoginGoogleUseCase
#### Situacao atual
Hoje `LoginGoogleUseCase`:
1. valida o `idToken`
2. busca o membro
3. salva o `refreshToken` se vier
4. gera o JWT interno

#### Mudanca proposta
`LoginGoogleUseCase` deve passar a:
1. receber o `authorizationCode`
2. trocar o codigo no Google por tokens
3. extrair o `id_token`
4. validar o `id_token` usando `AutenticarGoogleUseCase`
5. buscar o membro pelo email
6. salvar o `refresh_token`, se ele vier
7. gerar o JWT proprio da API

Em outras palavras, a logica deixa de ser:
- autenticar a partir de token fornecido pelo frontend

e passa a ser:
- autenticar a partir de tokens obtidos pelo proprio backend

### 5. Manter a validacao do id_token
`AutenticarGoogleUseCase` continua sendo valido no novo fluxo.

Ele ainda deve:
- validar a `audience`
- validar `email_verified`
- retornar os claims do Google ja confiaveis

Mudanca de origem:
- antes o `idToken` vinha do request do frontend
- agora o `id_token` vira da resposta do Google

### 6. Revisar configuracoes OAuth do backend
Hoje a configuracao Google existente contem:
- `clientId`
- `clientSecret`

Para o novo fluxo, convem revisar se sera necessario suportar tambem:
- `tokenUri`
- `redirectUri` padrao, se a aplicacao quiser fixar isso no backend

Possivel evolucao do record `GoogleOAuthProperties`:
- `clientId`
- `clientSecret`
- `tokenUri`

Observacao:
- `tokenUri` pode ser fixo no codigo se o time preferir simplicidade

### 7. Ajustar a logica de persistencia do refresh token
Hoje o backend salva o `refreshToken` se ele vier no request.

No novo fluxo, a origem muda:
- o `refreshToken` passa a vir da resposta do Google

Pontos de atencao:
- o Google pode nao enviar `refresh_token` em todos os logins
- a ausencia de `refresh_token` nao deve quebrar o login
- se ja existir token salvo, a regra de atualizacao deve ser explicita

Recomendacao de regra:
1. se o Google retornar `refresh_token`, atualizar o valor persistido
2. se o Google nao retornar `refresh_token`, manter o valor ja salvo
3. nao sobrescrever token existente com `null`

### 8. Rever o tratamento de erros do fluxo
#### Erros que ja existem
- `Token do Google invalido`
- `Token do Google nao pertence a aplicacao`
- `Email do Google nao verificado`
- `Membro não encontrado com o email fornecido.`

#### Novos erros do fluxo por authorization code
- `authorization code` invalido
- `authorization code` expirado
- `redirectUri` divergente
- falha de comunicacao com o Google
- resposta do Google sem `id_token`
- resposta do Google com erro OAuth2

Recomendacao:
- criar mensagens especificas para erros de troca de codigo
- diferenciar erro de autenticacao Google de erro interno de integracao

### 9. Atualizar testes automatizados
Os testes atuais de `LoginGoogleUseCase` refletem o fluxo antigo.

Novos cenarios de teste recomendados:
1. troca de `authorization code` com sucesso
2. resposta com `id_token` e `refresh_token`
3. resposta com `id_token` sem `refresh_token`
4. falha na troca do `authorization code`
5. `id_token` retornado, mas invalido
6. email valido no Google, mas sem `Membro` correspondente
7. garantia de que token salvo nao e sobrescrito por `null`

## Classes e logicas a serem modificadas
### DTO de entrada
- `applications/dtos/googleauth/GoogleAuthRequestDTO`

Mudanca:
- deixar de representar `idToken` e `refreshToken`
- passar a representar `authorizationCode`, `redirectUri` e possivel `codeVerifier`

### Controller
- `infrastructure/controllers/GoogleAuthController`

Mudanca:
- continuar com a rota `POST /api/v1/auth/google`
- passar a receber o novo DTO

### Caso de uso principal
- `usecases/googleauth/LoginGoogleUseCase`

Mudanca:
- trocar a entrada baseada em `idToken` por fluxo de `authorization code`
- incorporar a etapa de troca do codigo com o Google
- persistir `refreshToken` vindo da resposta do Google

### Validacao do token Google
- `usecases/googleauth/AutenticarGoogleUseCase`
- `infrastructure/auth/GoogleIdTokenVerifierImpl`

Mudanca:
- manter a validacao atual
- alterar apenas a origem do `id_token` validado

### Configuracao
- `global/config/GoogleOAuthProperties`

Mudanca:
- revisar necessidade de novos campos de configuracao

### Persistencia do refresh token
- `usecases/googleauth/AtualizarSecretGoogleUseCase`
- `domain/entity/GoogleRefreshTokenMembro`
- `domain/repository/GoogleRefreshTokenMembroRepository`
- `infrastructure/persistence/gateway/GoogleRefreshTokenMembroRepositoryImpl`

Mudanca:
- revisar a logica de atualizacao para suportar ausencia eventual de `refresh_token`

## Sequencia recomendada de implementacao
1. Criar o novo DTO de entrada baseado em `authorizationCode`.
2. Criar DTO de resposta de tokens do Google.
3. Criar a integracao HTTP para troca do codigo no Google.
4. Refatorar `LoginGoogleUseCase` para orquestrar o novo fluxo.
5. Ajustar a persistencia do `refreshToken`.
6. Revisar e expandir `GoogleOAuthProperties`.
7. Ajustar tratamento de erros.
8. Atualizar testes unitarios e de integracao.
9. Atualizar a documentacao do fluxo Google.

## Contrato esperado do frontend
O frontend deve passar a entregar ao backend:

```json
{
  "authorizationCode": "codigo-curto-recebido-do-google",
  "redirectUri": "https://app.exemplo.com/auth/google/callback",
  "codeVerifier": "opcional-se-pkce"
}
```

O frontend nao deve mais enviar ao backend:
- `idToken`
- `refreshToken`

Responsabilidade do frontend:
- iniciar corretamente o OAuth2 com Google
- obter o `authorization code`
- enviar ao backend o `redirectUri` exato usado no fluxo
- enviar `codeVerifier` quando PKCE for utilizado

Responsabilidade do backend:
- trocar o `authorization code`
- validar o `id_token`
- obter e persistir o `refresh_token`
- gerar o JWT interno da aplicacao

## Riscos e pontos de atencao
### Emissao do refresh token pelo Google
O Google pode nao enviar `refresh_token` em todos os logins. Isso depende do tipo de consentimento e da forma como o fluxo e iniciado no frontend.

Implicacao:
- o backend deve aceitar que alguns logins terminem sem novo `refresh_token`

### Redirect URI
Se o `redirectUri` enviado ao backend nao bater com o valor aceito pelo Google no momento da troca do codigo, a troca vai falhar.

Implicacao:
- frontend e backend precisam estar alinhados sobre esse valor

### PKCE
Se o frontend usar PKCE, o backend precisa receber o `codeVerifier` para concluir a troca do `authorization code`.

Implicacao:
- isso deve estar definido antes da implementacao

### Contrato externo
Essa mudanca quebra o contrato atual do endpoint de login Google.

Implicacao:
- frontend e backend devem ser versionados ou sincronizados na mudanca

## Conclusao
O novo fluxo por `authorization code` e uma evolucao correta para o cenario em que o backend precisa controlar a obtencao e persistencia do `refresh_token`.

Comparado ao fluxo atual, a principal mudanca e de responsabilidade:
- antes o frontend trazia os tokens
- agora o frontend entrega apenas o codigo, e o backend busca os tokens no Google

Com isso, o backend passa a ter mais controle sobre autenticacao externa, reduz dependencia do cliente para repasse de tokens sensiveis e prepara melhor a aplicacao para integracoes futuras com recursos do Google que dependam de `refresh_token`.
