OWASP_TOP_10

- id: A01
  nome: "Broken Access Control"
  status: "parcial"
  evidencias:
    - tipo: "codigo"
      local: "SecurityConfig.securityFilterChain"
      descricao: "Aplica .anyRequest().authenticated(), exigindo autenticação para rotas não públicas."
    - tipo: "codigo"
      local: "BuscarMembrosMinisterioPorEscalaEventoIdUseCase.validarEscalaEventoId"
      descricao: "Valida vínculo do líder com ministério via membroMinisterioRepository.buscarMinisterioLider(membroId, igrejaId)."
    - tipo: "codigo"
      local: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase.validarEscalaEventoId"
      descricao: "Bloqueia alteração de escala quando líder não pertence ao ministério da escala."
    - tipo: "codigo"
      local: "EscalaEventoController.buscarEscalaEventoConsolidadoPorMesAno"
      descricao: "Usa jwtUtils.getIgrejaId() e repassa igrejaId ao caso de uso para scoping por igreja."
    - tipo: "codigo"
      local: "BuscarTodosSemFiltroUseCase.execute"
      descricao: "Consulta membros por igreja via membroRepository.findByIgrejaIdExterno(jwtUtils.getIgrejaId(), pageable)."
  falhas:
    - "BuscarMembroPorUUIDUseCase.execute usa membroRepository.findByIdExterno(idExterno) sem validar igreja do token, permitindo IDOR por UUID conhecido."
    - "BuscarEventoEspecificoUseCase.execute usa eventoRepository.findByIdExterno(id) sem filtro por igreja do token."
    - "ApagarEventoUnicoUseCase.execute chama eventoRepository.deleteByIdExterno(id) sem condicionar à igreja autenticada."
    - "ApagarEventosMultiplosUseCase.execute inicia por eventoRepository.findByIdExterno(idEvento) sem scoping por igreja e, em seguida, pode adicionar esse evento na lista para deleteAll(eventos), permitindo exclusão cross-tenant por UUID conhecido."
    - "AtualizarMembroUseCase.execute atualiza membro por UUID sem checagem explícita de igreja/escopo do solicitante."
    - "Ausência de @PreAuthorize ativo nos controllers (anotações comentadas em EventoController e DashboardController), sem enforcement declarativo por papel/escopo."
  impacto:
    - "acesso indevido"
    - "exposicao de dados"
  recomendacoes:
    - "Substituir findByIdExterno/deleteByIdExterno por métodos com igreja: findByIdExternoAndIgrejaId/deleteByIdExternoAndIgrejaId."
    - "Ativar autorização por escopo com @PreAuthorize nos endpoints sensíveis (governo, líder e operações destrutivas)."
    - "Impedir alteração de fkIgreja em AtualizarMembroUseCase para usuários não-governo e validar ownership do recurso."

- id: A02
  nome: "Cryptographic Failures"
  status: "parcial"
  evidencias:
    - tipo: "codigo"
      local: "ValidarCriacaoMembro.hashSenha"
      descricao: "Senha persistida com BCryptPasswordEncoder.encode()."
    - tipo: "codigo"
      local: "GenerateTokenUseCase.execute"
      descricao: "JWT assinado com RSA (JwtEncoder/NimbusJwtEncoder) e expiração configurável (app.jwt.expiration-seconds)."
    - tipo: "codigo"
      local: "SecurityConfig.jwtEncoder / SecurityConfig.jwtDecoder"
      descricao: "Configura assinatura e validação de JWT com par de chaves RSA."
  falhas:
    - "Chave privada JWT está versionada em src/main/resources/testeprivate.key, expondo material criptográfico sensível no código-fonte."
    - "application.properties referencia chaves locais de teste (jwt.private.key / jwt.public.key), sem evidência de segregação obrigatória por ambiente."
  impacto:
    - "acesso indevido"
    - "exposicao de dados"
  recomendacoes:
    - "Remover chaves privadas do repositório e carregar segredos por cofre (Vault/KMS/Secrets Manager)."
    - "Rotacionar imediatamente o par de chaves RSA comprometido e invalidar tokens antigos."
    - "Definir política de gestão de chaves por ambiente e bloquear startup em produção com chaves de teste."

- id: A03
  nome: "Injection"
  status: "parcial"
  evidencias:
    - tipo: "codigo"
      local: "MembroJpaRepository.findAllWithFilter"
      descricao: "Consulta JPQL parametrizada com @Param, sem concatenação de SQL cru."
    - tipo: "codigo"
      local: "EventoJpaRepository.findByPeriodo / findByPeriodoAndRecorrencia"
      descricao: "Uso de parâmetros tipados em JPQL para período e igreja."
    - tipo: "codigo"
      local: "EventoController.criarEvento / atualizarEvento"
      descricao: "DTOs recebem @Valid, reduzindo payload inválido em entrada de usuário."
  falhas:
    - "LoginController.login e GoogleAuthController.autenticarComGoogle não aplicam @Valid no body, sem camada explícita de validação de formato/tamanho de entrada nesses fluxos."
    - "Não há evidência de sanitização específica para dados que seguem para logs/telemetria; existem prints diretos de valores em System.out."
  impacto:
    - "acesso indevido"
    - "exposicao de dados"
  recomendacoes:
    - "Aplicar @Valid e constraints em LoginRequestDTO e GoogleAuthRequestDTO para reduzir vetores de payload malformado."
    - "Remover logs de dados de entrada e usar logger estruturado com mascaramento de campos sensíveis."
    - "Manter somente queries parametrizadas e proibir createNativeQuery/concatenação em revisão estática."

- id: A04
  nome: "Insecure Design"
  status: "parcial"
  evidencias:
    - tipo: "fluxo"
      local: "SALVAR_ESCALA_MINISTERIO"
      descricao: "Fluxo possui regras de negócio de segurança: valida líder do ministério, bloqueia membros fora do ministério e conflito de escala."
    - tipo: "fluxo"
      local: "LOGIN_GOOGLE"
      descricao: "Fluxo valida audience do ID token e email verificado antes de emitir JWT interno."
    - tipo: "codigo"
      local: "AtualizarEscalaEventoPorEventoIdUseCase.buscarEvento"
      descricao: "Valida igreja do evento contra igrejaId do token antes de atualizar escalas."
  falhas:
    - "Modelo de autorização é inconsistente entre fluxos: algumas operações validam igreja/liderança e outras operam por UUID global sem scoping."
    - "Não existe política centralizada de autorização por recurso (ABAC/RBAC), dependência de checagens manuais por caso de uso."
    - "Operações administrativas de ministério não evidenciam validação de papel GOVERNO no próprio endpoint/usecase."
  impacto:
    - "acesso indevido"
    - "exposicao de dados"
  recomendacoes:
    - "Definir matriz de autorização por endpoint/ação e aplicar via policy central (Spring Method Security + service guard)."
    - "Padronizar regra multi-tenant: toda leitura/escrita por idExterno deve incluir igrejaId do token."
    - "Adicionar testes de autorização por fluxo (usuário de outra igreja, membro sem papel, líder sem vínculo)."

- id: A05
  nome: "Security Misconfiguration"
  status: "parcial"
  evidencias:
    - tipo: "codigo"
      local: "SecurityConfig.securityFilterChain"
      descricao: "Sessão stateless (SessionCreationPolicy.STATELESS) e resource server JWT ativos."
    - tipo: "codigo"
      local: "SecurityConfig.corsConfigurationSource"
      descricao: "CORS limitado ao origin http://localhost:5173 em configuração atual."
  falhas:
    - "CSRF desabilitado globalmente em SecurityConfig (csrf.disable())."
    - "Endpoints /h2-console/** e /swagger-ui/** estão permitidos sem autenticação."
    - "SecurityConfig desabilita frameOptions nos headers (frame.disable()), reduzindo proteção contra clickjacking."
    - "application.properties habilita H2 console (spring.h2.console.enabled=true)."
    - "application.properties expõe detalhes de erro (server.error.include-message=always e include-binding-errors=always)."
    - "Credencial RabbitMQ padrão em texto claro (spring.rabbitmq.username=guest / password=guest)."
  impacto:
    - "acesso indevido"
    - "exposicao de dados"
  recomendacoes:
    - "Restringir swagger e h2-console por perfil de ambiente e autenticação forte."
    - "Desabilitar include-message/include-binding-errors em produção."
    - "Externalizar credenciais de RabbitMQ para secret manager e remover defaults fracos."
    - "Avaliar CSRF por tipo de cliente; para fluxos com cookies/sessão futura, reativar proteção."

- id: A06
  nome: "Vulnerable and Outdated Components"
  status: "nao_adotado"
  evidencias:
    - tipo: "codigo"
      local: "pom.xml"
      descricao: "Dependências declaradas (Spring Boot, springdoc, mariadb-java-client), porém sem ferramenta de varredura de vulnerabilidades no build."
  falhas:
    - "Não há evidência de SCA automatizado (OWASP Dependency-Check, Snyk, osv-scanner) no pipeline Maven."
    - "Não há lock/política de atualização e bloqueio de CVEs conhecidas por severidade."
  impacto:
    - "acesso indevido"
    - "exposicao de dados"
  recomendacoes:
    - "Adicionar plugin de análise de dependências com falha de build para CVSS alto/crítico."
    - "Implementar rotina de atualização contínua de dependências e SBOM."
    - "Registrar baseline de versões aprovadas e janela máxima de atualização."

- id: A07
  nome: "Identification and Authentication Failures"
  status: "parcial"
  evidencias:
    - tipo: "codigo"
      local: "LoginServiceUseCase.execute"
      descricao: "Autenticação local com comparação BCryptPasswordEncoder.matches e emissão de JWT."
    - tipo: "codigo"
      local: "AutenticarGoogleUseCase.validarAudience / validarEmailVerificado"
      descricao: "Valida audience do token Google e exige email verificado."
    - tipo: "codigo"
      local: "SecurityConfig.oauth2ResourceServer"
      descricao: "JWT resource server ativo para autenticação de requisições protegidas."
  falhas:
    - "Não há evidência de rate limiting, lockout por tentativas, CAPTCHA ou MFA nos endpoints /api/v1/auth/login e /api/v1/auth/google."
    - "Fluxo de login diferencia respostas para usuário inexistente e senha inválida (ObjectNotFoundException vs BadCredentialsException), permitindo enumeração de contas."
    - "Scopes de JWT são emitidos (claim scope), mas não há enforcement ativo por @PreAuthorize nos endpoints com anotações comentadas."
  impacto:
    - "acesso indevido"
    - "exposicao de dados"
  recomendacoes:
    - "Uniformizar resposta de falha de login para evitar enumeração de usuário."
    - "Adicionar rate limit e bloqueio progressivo por IP/conta nos endpoints de autenticação."
    - "Ativar autorização por scope/papel com @PreAuthorize e testes de acesso negativo."

- id: A08
  nome: "Software and Data Integrity Failures"
  status: "parcial"
  evidencias:
    - tipo: "codigo"
      local: "GenerateTokenUseCase.execute"
      descricao: "Token JWT interno é assinado digitalmente (integridade do token)."
    - tipo: "codigo"
      local: "EventoProducer.publicarEventoCriadoAposCommit"
      descricao: "Publicação em mensageria ocorre após commit transacional, reduzindo inconsistência entre banco e evento."
  falhas:
    - "Chave privada de assinatura em repositório compromete a integridade da cadeia de confiança dos JWTs."
    - "Não há evidência de verificação de integridade de artefatos/dependências no build/deploy."
  impacto:
    - "acesso indevido"
    - "exposicao de dados"
  recomendacoes:
    - "Mover chaves para HSM/KMS e rotacionar periodicamente."
    - "Adicionar assinatura/verificação de artefatos e política de provenance no pipeline."
    - "Implementar validações de integridade de dependências com bloqueio automático."

- id: A09
  nome: "Security Logging and Monitoring Failures"
  status: "nao_adotado"
  evidencias:
    - tipo: "codigo"
      local: "RestExceptionHandler.oauth2AuthenticationHandler"
      descricao: "Existe tratamento de exceção, porém com System.err e sem padrão de auditoria de segurança."
    - tipo: "codigo"
      local: "BuscarMembrosMinisterioDisponiveisPorEscalaEventoIdUseCase.execute"
      descricao: "Uso de System.out para dados operacionais, sem trilha estruturada."
  falhas:
    - "Ausência de logging estruturado central (Logger/SLF4J) para eventos de autenticação, autorização e ações críticas."
    - "Ausência de trilha de auditoria para operações sensíveis (criação/edição/exclusão de evento, gestão de ministério, escalas)."
    - "Sem evidência de monitoramento/alertas de segurança (falhas repetidas de login, tentativas de acesso negado, anomalias)."
  impacto:
    - "acesso indevido"
    - "exposicao de dados"
  recomendacoes:
    - "Substituir System.out/System.err por logger estruturado com correlation-id."
    - "Criar eventos de auditoria para autenticação, autorização, alteração de perfil, eventos e ministérios."
    - "Integrar logs com SIEM e alertas de comportamento suspeito."

- id: A10
  nome: "Server-Side Request Forgery (SSRF)"
  status: "adotado"
  evidencias:
    - tipo: "codigo"
      local: "GoogleIdTokenVerifierImpl.GoogleIdTokenVerifierImpl"
      descricao: "Issuer OIDC fixo em https://accounts.google.com, sem URL controlada por usuário."
    - tipo: "codigo"
      local: "Pesquisa em src/main/java"
      descricao: "Não há evidência de uso de RestTemplate/WebClient/HttpURLConnection com endpoint derivado de input do usuário."
  falhas:
    - "Nao foram encontradas falhas SSRF exploráveis nos fluxos analisados, pois não existe chamada outbound com URL fornecida por usuário."
  impacto:
    - "acesso indevido"
    - "exposicao de dados"
  recomendacoes:
    - "Manter allowlist de destinos externos e proibir URL dinâmica em integrações futuras."
    - "Adicionar testes de segurança para prevenir regressão SSRF em novos conectores HTTP."

--------------------------------------------------

ANALISE_POR_FLUXO

- fluxo: "LOGIN_EMAIL_SENHA"
  endpoint: "POST /api/v1/auth/login"
  riscos_identificados:
    - tipo_owasp: "A07"
      descricao: "Enumeração de usuário por diferença de resposta entre email inexistente e senha incorreta."
      local: "BuscarPorEmaiUseCase.execute + LoginServiceUseCase.execute"
    - tipo_owasp: "A07"
      descricao: "Ausência de rate limiting/lockout no endpoint de autenticação."
      local: "LoginController.login"
  protecoes_existentes:
    - "Comparação de senha com BCryptPasswordEncoder.matches em LoginServiceUseCase.execute"
    - "JWT com expiração emitido por GenerateTokenUseCase.execute"
  lacunas:
    - "Sem @Valid em LoginRequestDTO no controller"
    - "Sem MFA/captcha/bloqueio por tentativas"

- fluxo: "LOGIN_GOOGLE"
  endpoint: "POST /api/v1/auth/google"
  riscos_identificados:
    - tipo_owasp: "A07"
      descricao: "Sem rate limiting para tentativa de token inválido em alta frequência."
      local: "GoogleAuthController.autenticarComGoogle"
    - tipo_owasp: "A09"
      descricao: "Falhas OAuth2 registradas sem padrão de auditoria central."
      local: "RestExceptionHandler.oauth2AuthenticationHandler"
  protecoes_existentes:
    - "Validação de audience do ID token em AutenticarGoogleUseCase.validarAudience"
    - "Validação de email verificado em AutenticarGoogleUseCase.validarEmailVerificado"
  lacunas:
    - "Sem @Valid no payload GoogleAuthRequestDTO"
    - "Sem monitoramento de tentativas falhas por IP/usuário"

- fluxo: "CONSULTA_MEMBROS"
  endpoint: "GET /api/v1/membros e GET /api/v1/membros/{idExterno}"
  riscos_identificados:
    - tipo_owasp: "A01"
      descricao: "Busca por UUID sem scoping por igreja pode expor membro de outra igreja."
      local: "BuscarMembroPorUUIDUseCase.execute"
    - tipo_owasp: "A04"
      descricao: "Controle de acesso inconsistente entre listagem por igreja e detalhe por UUID global."
      local: "MembroController.buscarTodos vs buscarPorId"
  protecoes_existentes:
    - "Listagem paginada filtrada por igreja em BuscarTodosSemFiltroUseCase.execute"
    - "Filtros por busca/status/ministerio com query parametrizada em MembroJpaRepository.findAllWithFilter"
  lacunas:
    - "Endpoint de detalhe não valida igreja do token"

- fluxo: "CRIACAO_ATUALIZACAO_EXCLUSAO_EVENTO"
  endpoint: "POST /api/v1/eventos, PATCH /api/v1/eventos/{id}, DELETE /api/v1/eventos/unico/{id}, DELETE /api/v1/eventos/multiplos/{id}"
  riscos_identificados:
    - tipo_owasp: "A01"
      descricao: "Exclusão de evento único por UUID sem filtro de igreja no delete."
      local: "ApagarEventoUnicoUseCase.execute + EventoRepository.deleteByIdExterno"
    - tipo_owasp: "A01"
      descricao: "Exclusão em lote pode incluir evento de outra igreja ao buscar por UUID global e executar deleteAll sem revalidar escopo do evento inicial."
      local: "ApagarEventosMultiplosUseCase.execute"
    - tipo_owasp: "A01"
      descricao: "Busca/atualização de evento por UUID global em parte dos fluxos."
      local: "BuscarEventoEspecificoUseCase.execute e AtualizarEventoUseCase.buscarEventoPorUUID"
    - tipo_owasp: "A09"
      descricao: "Sem auditoria de operações destrutivas de evento."
      local: "EventoController"
  protecoes_existentes:
    - "Validações de recorrência/horário/endereço no CriarEventoUseCase e ValidarHora"
  lacunas:
    - "Sem autorização declarativa por papel para ações de escrita/destruição"
    - "Delete único não restringe por igreja"
    - "Delete múltiplo inicia com findByIdExterno global e pode excluir evento fora da igreja do token"

- fluxo: "SALVAR_ESCALA_MINISTERIO"
  endpoint: "PATCH /api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}"
  riscos_identificados:
    - tipo_owasp: "A01"
      descricao: "Risco reduzido por validações fortes de vínculo; endpoint depende de checagem manual por caso de uso."
      local: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase.validarEscalaEventoId"
    - tipo_owasp: "A04"
      descricao: "Política de autorização concentrada em lógica de caso de uso sem camada central declarativa."
      local: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase"
  protecoes_existentes:
    - "Valida líder do ministério na igreja"
    - "Valida membros pertencentes ao ministério e conflito de escala"
    - "Recalcula status agregado em EscalaStatusDomainServiceImpl"
  lacunas:
    - "Ausência de @PreAuthorize ativo para defesa em profundidade"

- fluxo: "GESTAO_MINISTERIOS"
  endpoint: "POST /api/v1/ministerios/governo, PATCH /api/v1/ministerios/governo/{idMinisterio}, PATCH/DELETE /api/v1/ministerios/lider-ministerio/..."
  riscos_identificados:
    - tipo_owasp: "A01"
      descricao: "Criação/edição de ministério sem validação explícita de papel governo no endpoint/usecase."
      local: "AdicionarMinisterioUseCase.execute e EditarMinisterioUseCase.execute"
    - tipo_owasp: "A01"
      descricao: "Consultas por idExterno de membro/ministério sem scoping por igreja em métodos de apoio."
      local: "MembroJpaRepository.findByIdExterno e MinisteriosRepository.findByIdExterno"
  protecoes_existentes:
    - "Listagens governo/membro usam jwtUtils.getIgrejaId em usecases dedicados"
    - "Remoção de membro-ministério exige vínculo existente para efetivar delete"
  lacunas:
    - "Sem enforcement declarativo de cargo/escopo no controller"
    - "Sem validação sistemática de igreja para todos os caminhos de edição"

--------------------------------------------------

SUPERFICIE_ATAQUE

- tipo: "endpoint"
  local: "POST /api/v1/auth/login"
  validacao_existente: false
  sanitizacao: false
  autenticacao: false
  autorizacao: false
  observacoes: "Endpoint público; não aplica @Valid no body e não possui limitação de tentativas."

- tipo: "endpoint"
  local: "POST /api/v1/auth/google"
  validacao_existente: false
  sanitizacao: false
  autenticacao: false
  autorizacao: false
  observacoes: "Endpoint público; valida token Google no usecase, mas sem @Valid/rate limit no controller."

- tipo: "endpoint"
  local: "GET /api/v1/membros/{idExterno}"
  validacao_existente: true
  sanitizacao: false
  autenticacao: true
  autorizacao: false
  observacoes: "Autenticado globalmente, porém sem scoping por igreja na busca por UUID no usecase."

- tipo: "endpoint"
  local: "DELETE /api/v1/eventos/unico/{id}"
  validacao_existente: true
  sanitizacao: false
  autenticacao: true
  autorizacao: false
  observacoes: "Valida formato do id, mas delete por UUID não filtra igreja."

- tipo: "endpoint"
  local: "PATCH /api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}"
  validacao_existente: true
  sanitizacao: false
  autenticacao: true
  autorizacao: true
  observacoes: "Usecase valida vínculo líder-ministério e membresia da escala por igreja."

- tipo: "integracao_externa"
  local: "GoogleIdTokenVerifierImpl (JwtDecoders.fromOidcIssuerLocation)"
  validacao_existente: true
  sanitizacao: false
  autenticacao: true
  autorizacao: false
  observacoes: "Destino fixo (accounts.google.com), sem URL controlada por usuário, reduzindo SSRF."

- tipo: "integracao_externa"
  local: "EventoProducer.publicarEventoCriadoAposCommit"
  validacao_existente: true
  sanitizacao: false
  autenticacao: false
  autorizacao: false
  observacoes: "Publica evento em RabbitMQ após commit; sem evidência de assinatura/política de integridade da mensagem."

- tipo: "endpoint"
  local: "DELETE /api/v1/eventos/multiplos/{id}"
  validacao_existente: true
  sanitizacao: false
  autenticacao: true
  autorizacao: false
  observacoes: "Fluxo usa findByIdExterno inicial sem scoping por igreja e pode incluir evento fora do tenant na deleção em lote."

- tipo: "input_usuario"
  local: "DTOs com Bean Validation (MembroCreateDTO, CadastroExternoDTO, EventoCreateDTO)"
  validacao_existente: true
  sanitizacao: false
  autenticacao: depende_do_endpoint
  autorizacao: depende_do_endpoint
  observacoes: "Há validações de formato e obrigatoriedade em múltiplos fluxos, mas sem sanitização explícita e cobertura incompleta em login."

--------------------------------------------------

RESUMO_GERAL

- nivel_risco: "alto"
- principais_falhas:
  - "IDOR/multi-tenant incompleto em buscas e exclusões por UUID sem filtro de igreja."
  - "Delete múltiplo de eventos permite cenário cross-tenant ao iniciar por UUID global e executar deleteAll sem revalidar ownership do evento inicial."
  - "Ausência de autorização declarativa por papel/escopo em endpoints críticos (anotações @PreAuthorize comentadas)."
  - "Material criptográfico sensível (chave privada JWT) versionado no repositório."
  - "Ausência de trilha de auditoria e monitoramento de segurança; uso de System.out/System.err."
- pontos_fortes:
  - "Autenticação JWT stateless implementada e uso de BCrypt para senhas."
  - "Validações de entrada com Bean Validation em boa parte dos DTOs de negócio."
  - "Fluxos de escala possuem checagens concretas de vínculo líder/ministério/igreja e conflito de horário."
  - "Integração Google valida audience e email verificado antes de autenticar usuário local."
- prioridades_correcao:
  - "1) Corrigir A01: aplicar scoping obrigatório por igreja em todos os métodos por UUID (find/update/delete) e ativar @PreAuthorize."
  - "2) Corrigir A02/A08: remover e rotacionar chaves JWT do repositório; adotar secret manager."
  - "3) Corrigir A07: implementar rate limiting e resposta uniforme no login para evitar enumeração."
  - "4) Corrigir A09: implementar logging estruturado, auditoria de ações críticas e monitoramento/alertas."
  - "5) Corrigir A05/A06: endurecer configuração de produção (h2/swagger/erros) e adicionar SCA no pipeline Maven."