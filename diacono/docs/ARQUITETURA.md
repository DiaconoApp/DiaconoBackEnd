1. CAMADAS

- nome: domain
  responsabilidade: "Regras centrais de negócio, entidades, enums, contratos de repositório e serviço de domínio."
  principais_classes: [Membro, Ministerio, Evento, EscalaEvento, EscalaMinisterio, Igreja, Recorrencia, EnderecoMembro, EnderecoEvento, EnderecoIgreja, GoogleRefreshTokenMembro, MembroMinisterio, EnumCargoMembro, EnumCargoMembroMinisterio, EnumGeneroMembro, EnumStatusMembro, EnumStatusMinisterio, EnumStatusEvento, EnumStatusEscalaMinisterio, TipoRecorrencia, MembroRepository, MinisteriosRepository, MembroMinisterioRepository, EventoRepository, EscalaEventoRepository, EscalaMinisterioRepository, EnderecoEventoRepository, IgrejaRepository, RecorrenciaRepository, GoogleRefreshTokenMembroRepository, EscalaStatusDomainService, EscalaStatusDomainServiceImpl]
- nome: application
  responsabilidade: "Orquestração de casos de uso, validações de aplicação, mapeamentos e DTOs."
  principais_classes: [BuscarPerfilUseCase, GenerateTokenUseCase, LoginServiceUseCase, LoginGoogleUseCase, AutenticarGoogleUseCase, AtualizarSecretGoogleUseCase, CriarMembroUseCase, CadastrarMembroUseCase, AtualizarMembroUseCase, BuscarMembroPorUUIDUseCase, BuscarPorEmaiUseCase, BuscarTodosSemFiltroUseCase, BuscarTodosComFiltroUseCase, BuscasIgrejasUseCase, BuscarIgrejaPorUUIDUseCase, CriarEventoUseCase, AtualizarEventoUseCase, BuscarEventosPorMesEAnoUseCase, BuscarEventoEspecificoUseCase, BuscarEnderecoEventoUseCase, BuscarEnderecoEventoPorUUIDUseCase, BuscarMinisterioPorUUIDUseCase, ApagarEventoUnicoUseCase, ApagarEventosMultiplosUseCase, GerarEscalaEventoUseCase, AtualizarEscalaEventoPorEventoIdUseCase, BuscarEscalaEventoConsolidadoPorMesAnoUseCase, BuscarEscalaEventoEscaladoPorEventoIdUseCase, SalvarEscalaMinisterioPorEscalaEventoIdUseCase, BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase, BuscarEscalaMinisterioPorMembroIdMesAnoUseCase, BuscarMembrosMinisterioDisponiveisPorEscalaEventoIdUseCase, BuscarMembrosMinisterioPorEscalaEventoIdUseCase, BuscarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase, RevisarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase, AdicionarMinisterioUseCase, EditarMinisterioUseCase, AdicionarMembroMinisterioLiderMinisterioUseCase, RemoverMembroMinisterioLiderMinisterioUseCase, BuscarMinisteriosGeraisUseCase, BuscarMinisteriosGovernoSemFiltroUseCase, BuscarMinisteriosGovernoComFiltroUseCase, BuscarMinisteriosLiderMinisterioUseCase, BuscarMinisteriosMembroUseCase, BuscarMembroMinisterioLiderMinisterioSemFiltroUseCase, BuscarMembroMinisterioLiderMinisterioComFiltroUseCase, BuscarKpiMembrosDashUseCase, BuscarKpiEvolucaoMembrosDashUseCase, BuscarKpiFaixaEtariaMembrosDashUseCase, BuscarKpiGeneroMembrosDashUseCase, BuscarKpiMinisteriosDashUseCase, BuscarKpiEvolucaoMinisteriosDashUseCase, BuscarKpiQuantidadeMembrosPorMinisterioDashUseCase, buscarKpiQuantidadeEventosPorMinisterioDashUseCase, DashboardPeriodoValidator, ValidarHora, ValidarMesEAno, ValidarCriacaoMembro, ValidarIdExternoPreenchido]
- nome: infrastructure
  responsabilidade: "Implementações técnicas de persistência, autenticação, mensageria, segurança, criptografia, tratamento de erros e integração com framework."
  principais_classes: [MembroRepositoryImpl, MinisteriosRepositoryImpl, MembroMinisterioRepositoryImpl, EventoRepositoryImpl, EscalaEventoRepositoryImpl, EscalaMinisterioRepositoryImpl, EnderecoEventoRepositoryImpl, IgrejaRepositoryImpl, RecorrenciaRepositoryImpl, GoogleRefreshTokenMembroRepositoryImpl, MembroJpaRepository, MinisteriosJpaRepository, MembroMinisterioJpaRepository, EventoJpaRepository, EscalaEventoJpaRepository, EscalaMinisterioJpaRepository, EnderecoEventoJpaRepository, IgrejaJpaRepository, RecorrenciaJpaRepository, GoogleRefreshTokenMembroJpaRepository, GoogleIdTokenVerifier, GoogleIdTokenVerifierImpl, GoogleAuthorizationCodeExchanger, GoogleAuthorizationCodeExchangerImpl, EventoProducer, SecurityConfig, RabbitMQConfig, GoogleOAuthProperties, SensitiveFieldCryptoConfiguration, SensitiveDataStartupRunner, JwtUtils, IdEntityUtils, SensitiveFieldCryptoUtils, SensitiveSearchIndexUtils, SensitiveStringAttributeConverter, RestExceptionHandler, CustomOidcUserService, CustomOAuth2AuthenticationSuccessHandler, CustomMembroOAuth2User]
- nome: interfaces
  responsabilidade: "Exposição de endpoints HTTP e entrada/saída da API."
  principais_classes: [MembroController, EventoController, MinisteriosController, DashboardController, EscalaEventoController, EscalaMinisterioController, LoginController, GoogleAuthController, CadastroController, PerfilController]

2. DEPENDENCIAS ENTRE CAMADAS

- origem: interfaces
  depende_de: [application, domain, infrastructure]
- origem: application
  depende_de: [domain, infrastructure]
- origem: infrastructure
  depende_de: [application, domain]
- origem: domain
  depende_de: [application, infrastructure]

3. FLUXOS PRINCIPAIS

- nome: "Listagem de igrejas para cadastro externo"
  controller: "CadastroController.buscarIgrejas"
  usecase: "BuscasIgrejasUseCase.execute"
  domain_services: []
  repositories: [IgrejaRepository]
- nome: "Cadastro de membro externo"
  controller: "CadastroController.cadastrarMembro"
  usecase: "CadastrarMembroUseCase.execute"
  domain_services: []
  repositories: [MembroRepository, IgrejaRepository]
- nome: "Cadastro de membro interno"
  controller: "MembroController.criarMembro"
  usecase: "CriarMembroUseCase.execute"
  domain_services: []
  repositories: [MembroRepository, MinisteriosRepository, MembroMinisterioRepository, IgrejaRepository]
- nome: "Atualização de membro"
  controller: "MembroController.atualizarMembro | PerfilController.atualizarPerfil"
  usecase: "AtualizarMembroUseCase.execute"
  domain_services: []
  repositories: [MembroRepository, MembroMinisterioRepository, MinisteriosRepository, EscalaMinisterioRepository]
- nome: "Consulta de membros"
  controller: "MembroController.buscarTodos"
  usecase: "BuscarTodosSemFiltroUseCase.execute | BuscarTodosComFiltroUseCase.execute"
  domain_services: []
  repositories: [MembroRepository]
- nome: "Consulta de membro por UUID"
  controller: "MembroController.buscarPorId"
  usecase: "BuscarMembroPorUUIDUseCase.execute"
  domain_services: []
  repositories: [MembroRepository]
- nome: "Consulta de perfil autenticado"
  controller: "PerfilController.buscarPerfil"
  usecase: "BuscarPerfilUseCase.execute"
  domain_services: []
  repositories: [MembroRepository]
- nome: "Criação de evento"
  controller: "EventoController.criarEvento"
  usecase: "CriarEventoUseCase.execute"
  domain_services: []
  repositories: [EventoRepository, MembroRepository, EnderecoEventoRepository, MinisteriosRepository, IgrejaRepository]
- nome: "Consulta de eventos por mês e ano"
  controller: "EventoController.buscarEventosPorMesEAno"
  usecase: "BuscarEventosPorMesEAnoUseCase.execute"
  domain_services: []
  repositories: [EventoRepository]
- nome: "Consulta de evento específico"
  controller: "EventoController.buscarEventoEspecifico"
  usecase: "BuscarEventoEspecificoUseCase.execute"
  domain_services: []
  repositories: [EventoRepository]
- nome: "Consulta de endereços de evento"
  controller: "EventoController.buscarEnderecoEvento"
  usecase: "BuscarEnderecoEventoUseCase.execute"
  domain_services: []
  repositories: [EnderecoEventoRepository, IgrejaRepository]
- nome: "Atualização de evento"
  controller: "EventoController.atualizarEvento"
  usecase: "AtualizarEventoUseCase.execute"
  domain_services: [EscalaStatusDomainService]
  repositories: [EventoRepository, EnderecoEventoRepository, MinisteriosRepository, EscalaEventoRepository]
- nome: "Exclusão de evento único"
  controller: "EventoController.apagarEventoUnico"
  usecase: "ApagarEventoUnicoUseCase.execute"
  domain_services: []
  repositories: [EventoRepository]
- nome: "Exclusão de eventos recorrentes"
  controller: "EventoController.apagarEventosMultiplos"
  usecase: "ApagarEventosMultiplosUseCase.execute"
  domain_services: []
  repositories: [EventoRepository]
- nome: "Dashboard de membros"
  controller: "DashboardController.buscarKpisMembros | DashboardController.buscarEvolucaoMembros | DashboardController.buscarFaixaEtariaMembros | DashboardController.buscarGeneroMembros"
  usecase: "BuscarKpiMembrosDashUseCase.execute | BuscarKpiEvolucaoMembrosDashUseCase.execute | BuscarKpiFaixaEtariaMembrosDashUseCase.execute | BuscarKpiGeneroMembrosDashUseCase.execute"
  domain_services: []
  repositories: [MembroRepository]
- nome: "Dashboard de ministérios"
  controller: "DashboardController.buscarKpisMinisterios | DashboardController.buscarEvolucaoMinisterio | DashboardController.buscarQuantidadeMembrosPorMinisterio | DashboardController.buscarQuantidadeEventosPorMinisterio"
  usecase: "BuscarKpiMinisteriosDashUseCase.execute | BuscarKpiEvolucaoMinisteriosDashUseCase.execute | BuscarKpiQuantidadeMembrosPorMinisterioDashUseCase.execute | buscarKpiQuantidadeEventosPorMinisterioDashUseCase.execute"
  domain_services: []
  repositories: [MinisteriosRepository, EventoRepository, MembroMinisterioRepository]
- nome: "Consulta de ministérios gerais e do membro"
  controller: "MinisteriosController.buscarMinisteriosGerais | MinisteriosController.buscarMinisteriosMembro | MinisteriosController.buscarMinisteriosLiderMinisterio"
  usecase: "BuscarMinisteriosGeraisUseCase.execute | BuscarMinisteriosMembroUseCase.execute | BuscarMinisteriosLiderMinisterioUseCase.execute"
  domain_services: []
  repositories: [MinisteriosRepository, MembroMinisterioRepository]
- nome: "Gestão de ministérios pelo governo"
  controller: "MinisteriosController.buscarMinisteriosGoverno | MinisteriosController.adicionarMinisterio | MinisteriosController.editarMinisterio"
  usecase: "BuscarMinisteriosGovernoSemFiltroUseCase.execute | BuscarMinisteriosGovernoComFiltroUseCase.execute | AdicionarMinisterioUseCase.execute | EditarMinisterioUseCase.execute"
  domain_services: []
  repositories: [MinisteriosRepository, MembroJpaRepository]
- nome: "Gestão de membros do ministério pelo líder"
  controller: "MinisteriosController.buscarMembroMinisterioLiderMinisterio | MinisteriosController.adicionarMembroMinisterioLiderMinisterio | MinisteriosController.removerMembroMinisterioLiderMinisterio"
  usecase: "BuscarMembroMinisterioLiderMinisterioSemFiltroUseCase.execute | BuscarMembroMinisterioLiderMinisterioComFiltroUseCase.execute | AdicionarMembroMinisterioLiderMinisterioUseCase.execute | RemoverMembroMinisterioLiderMinisterioUseCase.execute"
  domain_services: []
  repositories: [MembroMinisterioRepository, MinisteriosRepository, MembroJpaRepository]
- nome: "Consulta de escala de evento consolidada"
  controller: "EscalaEventoController.buscarEscalaEventoConsolidadoPorMesAno"
  usecase: "BuscarEscalaEventoConsolidadoPorMesAnoUseCase.execute"
  domain_services: []
  repositories: [EscalaEventoRepository]
- nome: "Consulta de escala de evento escalada"
  controller: "EscalaEventoController.buscarEscalaEventoEscaladoPorEventoId"
  usecase: "BuscarEscalaEventoEscaladoPorEventoIdUseCase.execute"
  domain_services: []
  repositories: [EscalaEventoRepository]
- nome: "Atualização de escala por evento"
  controller: "EscalaEventoController.atualizarEscalaEventoPorEventoId"
  usecase: "AtualizarEscalaEventoPorEventoIdUseCase.execute"
  domain_services: [EscalaStatusDomainService]
  repositories: [EventoRepository, EscalaMinisterioRepository, MinisteriosRepository, EscalaEventoRepository]
- nome: "Consulta de escala de ministério consolidada"
  controller: "EscalaMinisterioController.buscarEscalaMinisterioConsolidadoPorMesAno"
  usecase: "BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase.execute"
  domain_services: []
  repositories: [EscalaMinisterioRepository, MembroMinisterioRepository]
- nome: "Consulta de escala de ministério do membro"
  controller: "EscalaMinisterioController.buscarEscalaMinisterioPorMembroIdMesAno"
  usecase: "BuscarEscalaMinisterioPorMembroIdMesAnoUseCase.execute"
  domain_services: []
  repositories: [EscalaMinisterioRepository, MembroMinisterioRepository]
- nome: "Consulta e randomização de membros para escala de ministério"
  controller: "EscalaMinisterioController.buscarMembrosMinisterioDisponiveisPorEscalaEventoId | EscalaMinisterioController.buscarMembrosMinisterioPorEscalaEventoId | EscalaMinisterioController.buscarMembrosMinisterioRandomizadosPorEscalaEventoId | EscalaMinisterioController.RevisarMembrosMinisterioRandomizadosPorEscalaEventoId"
  usecase: "BuscarMembrosMinisterioDisponiveisPorEscalaEventoIdUseCase.execute | BuscarMembrosMinisterioPorEscalaEventoIdUseCase.execute | BuscarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase.execute | RevisarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase.execute"
  domain_services: []
  repositories: [EscalaMinisterioRepository, EscalaEventoRepository, MembroMinisterioRepository]
- nome: "Salvar escala de membros do ministério"
  controller: "EscalaMinisterioController.salvarEscalaMinisterioPorEscalaEventoId"
  usecase: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase.execute"
  domain_services: [EscalaStatusDomainService]
  repositories: [EscalaMinisterioRepository, EscalaEventoRepository, MembroMinisterioRepository, EventoRepository]
- nome: "Login por email e senha"
  controller: "LoginController.login"
  usecase: "LoginServiceUseCase.execute"
  domain_services: []
  repositories: [MembroRepository]
- nome: "Login com Google"
  controller: "GoogleAuthController.autenticarComGoogle"
  usecase: "LoginGoogleUseCase.execute"
  domain_services: []
  repositories: [MembroRepository, GoogleRefreshTokenMembroRepository]

4. VIOLACOES DE CLEAN ARCHITECTURE

- tipo: "dependencia indevida"
  local: "MembroRepository | EventoRepository | EscalaEventoRepository | EscalaMinisterioRepository | MinisteriosRepository | MembroMinisterioRepository"
  descricao: "Contratos de repositório na camada domain dependem de DTOs da camada applications; MembroRepository, MinisteriosRepository e MembroMinisterioRepository também dependem de Page/Pageable do Spring Data."
- tipo: "dependencia indevida"
  local: "Membro | GoogleRefreshTokenMembro | Evento | EnderecoEvento | EnderecoIgreja | EscalaEvento | EscalaMinisterio | Igreja | Ministerio | MembroMinisterio | Recorrencia | EnderecoMembro"
  descricao: "Entidades na camada domain dependem de utilitários e converters da camada técnica (IdEntityUtils, SensitiveSearchIndexUtils e SensitiveStringAttributeConverter em global.util)."
- tipo: "dependencia indevida"
  local: "AdicionarMinisterioUseCase.execute | AdicionarMembroMinisterioLiderMinisterioUseCase.execute | EditarMinisterioUseCase.execute"
  descricao: "Use cases dependem diretamente de MembroJpaRepository (infraestrutura), em vez de depender apenas de portas de domínio (MembroRepository)."
- tipo: "dependencia indevida"
  local: "CriarEventoUseCase.execute | LoginGoogleUseCase.execute | AutenticarGoogleUseCase.execute"
  descricao: "Use cases dependem diretamente de adaptadores técnicos em infrastructure.messaging e infrastructure.auth, em vez de portas de aplicação/domínio."
- tipo: "dependencia indevida"
  local: "BuscarPerfilUseCase.execute | CriarEventoUseCase.execute | BuscarKpiMembrosDashUseCase.execute | BuscarKpiMinisteriosDashUseCase.execute | BuscarKpiEvolucaoMembrosDashUseCase.execute | BuscarKpiEvolucaoMinisteriosDashUseCase.execute | BuscarKpiFaixaEtariaMembrosDashUseCase.execute | BuscarKpiGeneroMembrosDashUseCase.execute | BuscarKpiQuantidadeMembrosPorMinisterioDashUseCase.execute | buscarKpiQuantidadeEventosPorMinisterioDashUseCase.execute"
  descricao: "Use cases dependem de JwtUtils, acoplando a aplicação ao SecurityContext em vez de receber explicitamente o contexto autenticado."
- tipo: "dependencia indevida"
  local: "ValidarHora.validaHoraInicioMenorHoraFim | ValidarHora.validarHoraFuturo"
  descricao: "Validação de aplicação depende de exceção da infraestrutura (TimeInvalidException em infrastructure.exceptions)."
- tipo: "regra em controller"
  local: "MembroController.buscarTodos | MinisteriosController.buscarMinisteriosGoverno | MinisteriosController.buscarMembroMinisterioLiderMinisterio"
  descricao: "Controllers contêm regras de decisão para escolha de fluxo sem/com filtro, reduzindo a centralização das regras em use cases."
