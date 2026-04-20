1. CAMADAS

- nome: domain
  responsabilidade: "Regras centrais de negócio, entidades, contratos de repositório e serviço de domínio."
  principais_classes: [Membro, Ministerio, Evento, EscalaEvento, EscalaMinisterio, Igreja, Recorrencia, EnderecoMembro, EnderecoEvento, EnderecoIgreja, GoogleRefreshTokenMembro, MembroMinisterio, MembroRepository, MinisteriosRepository, FindMinisteriosMembrosRepository, EventoRepository, EscalaStatusDomainService, EscalaStatusDomainServiceImpl, EnderecoEventoRepository, IgrejaRepository, RecorrenciaRepository]
- nome: application
  responsabilidade: "Orquestração de casos de uso, validações de aplicação, mapeamentos e DTOs."
  principais_classes: [CriarMembroUseCase, CadastrarMembroUseCase, BuscarTodosSemFiltroUseCase, BuscarTodosComFiltroUseCase, CriarEventoUseCase, AtualizarEventoUseCase, BuscarKpiMembrosDashUseCase, BuscarKpiMinisteriosDashUseCase, BuscarKpiEvolucaoMembrosDashUseCase, BuscarKpiGeneroMembrosDashUseCase, BuscarKpiEvolucaoMinisteriosDashUseCase, SalvarEscalaMinisterioPorEscalaEventoIdUseCase, AtualizarEscalaEventoPorEventoIdUseCase, LoginServiceUseCase, LoginGoogleUseCase, GenerateTokenUseCase, BuscasIgrejasUseCase, BuscarMinisteriosMembroUseCase]
- nome: infrastructure
  responsabilidade: "Implementações técnicas de persistência, autenticação externa e integração com framework."
  principais_classes: [MembroRepositoryImpl, MinisteriosRepositoryImpl, FindMinisteriosMembrosRepositoryImpl, EventoRepositoryImpl, EscalaEventoRepositoryImpl, EscalaMinisterioRepositoryImpl, GoogleRefreshTokenMembroRepositoryImpl, GoogleIdTokenVerifierImpl, MembroJpaRepository, FindMinisteriosMembrosJpaRepository, SecurityConfig, JwtUtils]
- nome: interfaces
  responsabilidade: "Exposição de endpoints HTTP e entrada/saída da API."
  principais_classes: [MembroController, EventoController, MinisteriosController, DashboardController, EscalaEventoController, EscalaMinisterioController, LoginController, GoogleAuthController, CadastroController]

2. DEPENDENCIAS ENTRE CAMADAS

- origem: interfaces
  depende_de: [application, domain]
- origem: application
  depende_de: [domain, infrastructure]
- origem: infrastructure
  depende_de: [domain]
- origem: domain
  depende_de: []

3. FLUXOS PRINCIPAIS

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
- nome: "Consulta de membros"
  controller: "MembroController.buscarTodos"
  usecase: "BuscarTodosSemFiltroUseCase.execute | BuscarTodosComFiltroUseCase.execute"
  domain_services: []
  repositories: [MembroRepository]
- nome: "Criação de evento"
  controller: "EventoController.criarEvento"
  usecase: "CriarEventoUseCase.execute"
  domain_services: []
  repositories: [EventoRepository, MembroRepository, EnderecoEventoRepository, MinisteriosRepository, IgrejaRepository]
- nome: "Atualização de evento"
  controller: "EventoController.atualizarEvento"
  usecase: "AtualizarEventoUseCase.execute"
  domain_services: [EscalaStatusDomainService]
  repositories: [EventoRepository, EnderecoEventoRepository, MinisteriosRepository]
- nome: "Dashboard de membros (KPIs)"
  controller: "DashboardController.buscarKpisMembros"
  usecase: "BuscarKpiMembrosDashUseCase.execute"
  domain_services: []
  repositories: [MembroRepository]
- nome: "Dashboard de ministérios (KPIs)"
  controller: "DashboardController.buscarKpisMinisterios"
  usecase: "BuscarKpiMinisteriosDashUseCase.execute"
  domain_services: []
  repositories: [MinisteriosRepository, EventoRepository]
- nome: "Consulta de ministérios do membro"
  controller: "MinisteriosController.buscarMinisteriosMembro"
  usecase: "BuscarMinisteriosMembroUseCase.execute"
  domain_services: []
  repositories: [FindMinisteriosMembrosRepository]
- nome: "Consulta de escala de evento consolidada"
  controller: "EscalaEventoController.buscarEscalaEventoConsolidadoPorMesAno"
  usecase: "BuscarEscalaEventoConsolidadoPorMesAnoUseCase.execute"
  domain_services: []
  repositories: [EscalaEventoRepository]
- nome: "Atualização de escala por evento"
  controller: "EscalaEventoController.atualizarEscalaEventoPorEventoId"
  usecase: "AtualizarEscalaEventoPorEventoIdUseCase.execute"
  domain_services: [EscalaStatusDomainService]
  repositories: [EventoRepository, MinisteriosRepository]
- nome: "Salvar escala de membros do ministério"
  controller: "EscalaMinisterioController.salvarEscalaMinisterioPorEscalaEventoId"
  usecase: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase.execute"
  domain_services: [EscalaStatusDomainService]
  repositories: [EscalaMinisterioRepository, EscalaEventoRepository, MembroMinisterioRepository]
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
  descricao: "Contratos de repositório na camada domain dependem de DTOs da camada applications (import com.diacono.diacono.applications.dtos...)."
- tipo: "dependencia indevida"
  local: "AdicionarMinisterioUseCase.execute | AdicionarMembroMinisterioLiderMinisterioUseCase.execute | EditarMinisterioUseCase.atualizarLider"
  descricao: "Use cases dependem diretamente de MembroJpaRepository (infraestrutura), em vez de depender apenas de portas de domínio (MembroRepository)."
- tipo: "dependencia indevida"
  local: "ValidarHora.validaHoraInicioMenorHoraFim | ValidarHora.validarHoraFuturo"
  descricao: "Validação de aplicação depende de exceção da infraestrutura (TimeInvalidException em infrastructure.exceptions)."
- tipo: "regra em controller"
  local: "MinisteriosController.buscarMembroMinisterioLiderMinisterio | MembroController.buscarTodos"
  descricao: "Controllers contêm regras de decisão/validação de negócio (escolha de fluxo sem/com filtro e validação de id), reduzindo a centralização das regras em use cases."
- tipo: "outro"
  local: "DiaconoApplication.demo"
  descricao: "Classe de bootstrap contém criação de entidade, uso direto de MembroJpaRepository e dados hardcoded, misturando inicialização de infraestrutura com regra de domínio/aplicação."
