FLUXOS
- nome: "CADASTRO_MEMBRO_EXTERNO"
  descricao: "Cadastro de membro por fluxo publico de registro."
  entrada:
    endpoint: "/api/v1/register"
    metodo: "POST"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "CadastroController.cadastrarMembro"
    - passo: 2
      tipo: usecase
      nome: "CadastrarMembroUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "CadastrarMembroUseCase.criarMembroExterno"
    - passo: 4
      tipo: usecase
      nome: "BuscarIgrejaPorUUIDUseCase.execute"
    - passo: 5
      tipo: usecase
      nome: "ValidarCriacaoMembro.hashSenha"
    - passo: 6
      tipo: usecase
      nome: "ValidarCriacaoMembro.validaCriacao"
    - passo: 7
      tipo: repository
      nome: "MembroRepository.findByEmailOrCpf"
    - passo: 8
      tipo: repository
      nome: "IgrejaRepository.findByIdExterno"
    - passo: 9
      tipo: repository
      nome: "MembroRepository.save"
  entidades_afetadas: [Membro, Igreja]
  estados:
    antes: ["Membro inexistente para email/cpf", "Igreja existente"]
    depois: ["Membro status=ATIVO", "Membro cargo=Membro"]
  regras_aplicadas: ["cadastroDTO nao pode ser nulo", "email/cpf nao podem estar duplicados", "igreja informada deve existir", "senha deve ser hash antes de persistir"]
- nome: "CADASTRO_MEMBRO_INTERNO"
  descricao: "Cadastro de membro por endpoint autenticado de membros."
  entrada:
    endpoint: "/api/v1/membros"
    metodo: "POST"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "MembroController.criarMembro"
    - passo: 2
      tipo: usecase
      nome: "CriarMembroUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "CriarMembroUseCase.criarMembroSemMinisterio"
    - passo: 4
      tipo: usecase
      nome: "CriarMembroUseCase.criarMembroComMinisterio"
    - passo: 5
      tipo: usecase
      nome: "BuscarIgrejaPorUUIDUseCase.execute"
    - passo: 6
      tipo: usecase
      nome: "ValidarCriacaoMembro.hashSenha"
    - passo: 7
      tipo: usecase
      nome: "ValidarCriacaoMembro.validaCriacao"
    - passo: 8
      tipo: repository
      nome: "MembroRepository.findByEmailOrCpf"
    - passo: 9
      tipo: repository
      nome: "MinisteriosRepository.findByIdExterno"
    - passo: 10
      tipo: repository
      nome: "MembroRepository.save"
    - passo: 11
      tipo: repository
      nome: "MembroMinisterioRepository.deleteByMembro"
    - passo: 12
      tipo: repository
      nome: "MembroMinisterioRepository.save"
  entidades_afetadas: [Membro, MembroMinisterio, Ministerio, Igreja]
  estados:
    antes: ["Membro inexistente para email/cpf", "Ministerio opcional", "Igreja existente"]
    depois: ["Membro status=ATIVO", "MembroMinisterio criado quando idExternoMinisterios informado"]
  regras_aplicadas: ["membroDTO nao pode ser nulo", "lider de ministerio deve ter ministerio associado", "email/cpf nao podem estar duplicados", "ministerio informado deve existir", "hash de senha obrigatorio"]
- nome: "CONSULTA_MEMBROS"
  descricao: "Busca paginada de membros com ou sem filtro."
  entrada:
    endpoint: "/api/v1/membros"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "MembroController.buscarTodos"
    - passo: 2
      tipo: usecase
      nome: "BuscarTodosSemFiltroUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "BuscarTodosComFiltroUseCase.execute"
    - passo: 4
      tipo: usecase
      nome: "BuscarTodosSemFiltroUseCase.validarMembrosEncontradosPage"
    - passo: 5
      tipo: usecase
      nome: "BuscarTodosComFiltroUseCase.validarMembrosEncontradosList"
    - passo: 6
      tipo: repository
      nome: "MembroRepository.findByIgrejaIdExterno"
    - passo: 7
      tipo: repository
      nome: "MembroRepository.findAllWithFilter"
  entidades_afetadas: [Membro]
  estados:
    antes: ["Membros existentes na igreja do token"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["sem filtros chama fluxo sem filtro", "com filtros chama fluxo com filtro", "se lista/pagina vazia retorna erro de nao encontrado", "filtro por ministerio exige vinculo em MembroMinisterio"]
- nome: "CONSULTA_MINISTERIOS_MEMBRO"
  descricao: "Busca os ministérios do membro autenticado na igreja do token."
  entrada:
    endpoint: "/api/v1/ministerios/membro"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "MinisteriosController.buscarMinisteriosMembro"
    - passo: 2
      tipo: usecase
      nome: "BuscarMinisteriosMembroUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "BuscarMinisteriosMembroUseCase.buscarMinisteriosMembro"
    - passo: 4
      tipo: repository
      nome: "FindMinisteriosMembrosRepository.buscarMinisteriosMembro"
    - passo: 5
      tipo: repository
      nome: "FindMinisteriosMembrosJpaRepository.buscarMinisteriosMembro"
  entidades_afetadas: [Membro, MembroMinisterio, Ministerio]
  estados:
    antes: ["Membro autenticado com vínculo de igreja via JWT"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["consulta respeita o subject e a igreja do JWT", "retorno vazio gera ObjectNotFoundException", "resultado usa projeção direta para MinisterioSuperSimplificadoDTO"]
- nome: "CRIACAO_EVENTO"
  descricao: "Cria evento unico ou recorrente e gera escalas de evento."
  entrada:
    endpoint: "/api/v1/eventos"
    metodo: "POST"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "EventoController.criarEvento"
    - passo: 2
      tipo: usecase
      nome: "CriarEventoUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "CriarEventoUseCase.validarRecorrencia"
    - passo: 4
      tipo: usecase
      nome: "CriarEventoUseCase.validarEnderecoEvento"
    - passo: 5
      tipo: usecase
      nome: "ValidarHora.validaHoraInicioMenorHoraFim"
    - passo: 6
      tipo: usecase
      nome: "ValidarHora.validarHoraFuturo"
    - passo: 7
      tipo: usecase
      nome: "CriarEventoUseCase.criarEventoSemRecorrencia"
    - passo: 8
      tipo: usecase
      nome: "CriarEventoUseCase.criarEventoRecorrenciaSemanal"
    - passo: 9
      tipo: usecase
      nome: "CriarEventoUseCase.criarEventoRecorrenciaMensal"
    - passo: 10
      tipo: usecase
      nome: "GerarEscalaEventoUseCase.executeParaCriacao"
    - passo: 11
      tipo: repository
      nome: "EnderecoEventoRepository.findByIdExterno"
    - passo: 12
      tipo: repository
      nome: "MembroRepository.findByIdExterno"
    - passo: 13
      tipo: repository
      nome: "IgrejaRepository.findByIdExterno"
    - passo: 14
      tipo: repository
      nome: "MinisteriosRepository.findAllByIdExternoIn"
    - passo: 15
      tipo: repository
      nome: "EventoRepository.save"
    - passo: 16
      tipo: repository
      nome: "EventoRepository.saveAll"
  entidades_afetadas: [Evento, EscalaEvento, EnderecoEvento, Recorrencia, Membro, Igreja, Ministerio]
  estados:
    antes: ["Evento inexistente", "recorrencia e horario ainda nao validados"]
    depois: ["Evento criado", "EscalaEvento criada para ministerios selecionados", "Eventos recorrentes criados quando aplicavel"]
  regras_aplicadas: ["recorrencia deve respeitar datas e limite de 1 ano", "horario fim deve ser maior que inicio", "evento nao pode ser no passado", "endereco deve ser completo quando idExterno nao informado", "organizacao depende de membro/igreja do token"]
- nome: "ATUALIZACAO_EVENTO"
  descricao: "Atualiza dados do evento e recalcula status de confirmacao do evento."
  entrada:
    endpoint: "/api/v1/eventos/{id}"
    metodo: "PATCH"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "EventoController.atualizarEvento"
    - passo: 2
      tipo: usecase
      nome: "AtualizarEventoUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "ValidarIdExternoPreenchido.validarIdExternoPreenchido"
    - passo: 4
      tipo: usecase
      nome: "AtualizarEventoUseCase.validarEnderecoDiferente"
    - passo: 5
      tipo: usecase
      nome: "GerarEscalaEventoUseCase.executeParaAtualizacao"
    - passo: 6
      tipo: domain_service
      nome: "EscalaStatusDomainService.recalcularStatusEvento"
    - passo: 7
      tipo: repository
      nome: "EventoRepository.findByIdExterno"
    - passo: 8
      tipo: repository
      nome: "EnderecoEventoRepository.findByIdExterno"
    - passo: 9
      tipo: repository
      nome: "MinisteriosRepository.findAllByIdExternoIn"
    - passo: 10
      tipo: repository
      nome: "EventoRepository.save"
    - passo: 11
      tipo: repository
      nome: "EscalaEventoRepository.areAllConfirmadosByEventoId"
    - passo: 12
      tipo: repository
      nome: "EventoRepository.updateStatusByEventoId"
  entidades_afetadas: [Evento, EscalaEvento, EnderecoEvento, Ministerio]
  estados:
    antes: ["Evento existente", "Escalas de evento existentes"]
    depois: ["Campos do evento atualizados", "Escalas ajustadas quando fkMinisterios informado", "Status do evento recalculado"]
  regras_aplicadas: ["idExterno do evento deve ser valido", "endereco so altera quando houver diferenca", "atualizacao de escalas depende de fkMinisterios", "status do evento eh derivado da confirmacao das escalas"]
- nome: "DASHBOARD_MEMBROS_KPIS"
  descricao: "Calcula KPIs consolidados de membros no periodo."
  entrada:
    endpoint: "/api/v1/dashboards/membros/kpis"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "DashboardController.buscarKpisMembros"
    - passo: 2
      tipo: usecase
      nome: "BuscarKpiMembrosDashUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "DashboardPeriodoValidator.validarAnoInicioEFim"
    - passo: 4
      tipo: usecase
      nome: "BuscarKpiMembrosDashUseCase.buscarKpis"
    - passo: 5
      tipo: repository
      nome: "MembroRepository.buscarKpisMembros"
  entidades_afetadas: [Membro]
  estados:
    antes: ["Dados historicos de membros existentes"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["anoInicio e anoFim devem ser validos", "kpis sao calculados para igreja do token", "retencao depende de membrosAtivos e membrosInativos"]
- nome: "DASHBOARD_MINISTERIOS_KPIS"
  descricao: "Calcula KPIs consolidados de ministerios e eventos no periodo."
  entrada:
    endpoint: "/api/v1/dashboards/ministerios/kpis"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "DashboardController.buscarKpisMinisterios"
    - passo: 2
      tipo: usecase
      nome: "BuscarKpiMinisteriosDashUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "DashboardPeriodoValidator.validarAnoInicioEFim"
    - passo: 4
      tipo: usecase
      nome: "BuscarKpiMinisteriosDashUseCase.ministerioBuscarKpis"
    - passo: 5
      tipo: usecase
      nome: "BuscarKpiMinisteriosDashUseCase.buscarKpisEvento"
    - passo: 6
      tipo: repository
      nome: "MinisteriosRepository.buscarKpis"
    - passo: 7
      tipo: repository
      nome: "EventoRepository.buscarKpisEvento"
  entidades_afetadas: [Ministerio, Evento]
  estados:
    antes: ["Dados historicos de ministerios e eventos existentes"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["anoInicio e anoFim devem ser validos", "dados vazios no periodo geram erro de nao encontrado", "consultas restritas a igreja do token"]
- nome: "CONSULTA_ESCALA_EVENTO_CONSOLIDADA"
  descricao: "Consulta escalas de evento por mes/ano com filtros opcionais."
  entrada:
    endpoint: "/api/v1/escalas-evento/governo"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "EscalaEventoController.buscarEscalaEventoConsolidadoPorMesAno"
    - passo: 2
      tipo: usecase
      nome: "BuscarEscalaEventoConsolidadoPorMesAnoUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "ValidarMesEAno.validarMesEAno"
    - passo: 4
      tipo: repository
      nome: "EscalaEventoRepository.findEscalaEventoConsolidadoByPeriodo"
  entidades_afetadas: [EscalaEvento, Evento, Ministerio]
  estados:
    antes: ["Escalas de evento existentes"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["mes e ano devem ser validos", "filtros status/ministerio/nomeEvento sao opcionais", "consulta restrita a igreja do token"]
- nome: "ATUALIZACAO_ESCALA_EVENTO"
  descricao: "Atualiza ministerios escalados no evento e recalcula status agregado."
  entrada:
    endpoint: "/api/v1/escalas-evento/governo/{eventoId}"
    metodo: "PATCH"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "EscalaEventoController.atualizarEscalaEventoPorEventoId"
    - passo: 2
      tipo: usecase
      nome: "AtualizarEscalaEventoPorEventoIdUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "AtualizarEscalaEventoPorEventoIdUseCase.validarListaEscalaEvento"
    - passo: 4
      tipo: usecase
      nome: "AtualizarEscalaEventoPorEventoIdUseCase.buscarEvento"
    - passo: 5
      tipo: usecase
      nome: "GerarEscalaEventoUseCase.executeParaAtualizacao"
    - passo: 6
      tipo: domain_service
      nome: "EscalaStatusDomainService.recalcularStatusEvento"
    - passo: 7
      tipo: repository
      nome: "EventoRepository.findByIdExterno"
    - passo: 8
      tipo: repository
      nome: "MinisteriosRepository.findAllByIdExternoIn"
    - passo: 9
      tipo: repository
      nome: "EventoRepository.save"
    - passo: 10
      tipo: repository
      nome: "EscalaEventoRepository.areAllConfirmadosByEventoId"
    - passo: 11
      tipo: repository
      nome: "EventoRepository.updateStatusByEventoId"
  entidades_afetadas: [Evento, EscalaEvento, Ministerio]
  estados:
    antes: ["Evento e escalas existentes", "EscalaEvento possivelmente PENDENTE"]
    depois: ["Escalas do evento substituidas/atualizadas", "Status do Evento recalculado"]
  regras_aplicadas: ["lista de escala nao pode ser vazia", "evento deve pertencer a igreja do token", "somente ministerios marcados como escalados entram no recalculo", "status final do evento depende de confirmacao total"]
- nome: "SALVAR_ESCALA_MINISTERIO"
  descricao: "Salva membros escalados para um escalaEvento e propaga status de confirmacao."
  entrada:
    endpoint: "/api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}"
    metodo: "PATCH"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "EscalaMinisterioController.salvarEscalaMinisterioPorEscalaEventoId"
    - passo: 2
      tipo: usecase
      nome: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase.validarRequest"
    - passo: 4
      tipo: usecase
      nome: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase.validarEscalaEventoId"
    - passo: 5
      tipo: usecase
      nome: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase.validarMembrosDaEscala"
    - passo: 6
      tipo: usecase
      nome: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase.validarConflitoDeEscala"
    - passo: 7
      tipo: usecase
      nome: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase.buscarEscalaEvento"
    - passo: 8
      tipo: domain_service
      nome: "EscalaStatusDomainService.recalcularStatusPorEscalaEventoId"
    - passo: 9
      tipo: repository
      nome: "EscalaEventoRepository.findMinisterioIdByEscalaEventoId"
    - passo: 10
      tipo: repository
      nome: "MembroMinisterioRepository.buscarMinisterioLider"
    - passo: 11
      tipo: repository
      nome: "EscalaMinisterioRepository.findEscalaMembroMinisterioByEscalaEventoId"
    - passo: 12
      tipo: repository
      nome: "EscalaMinisterioRepository.findMembrosMinisterioOcupadosByEscalaEventoId"
    - passo: 13
      tipo: repository
      nome: "EscalaEventoRepository.findEscalaEventoByIdExternoAndIgrejaId"
    - passo: 14
      tipo: repository
      nome: "EscalaMinisterioRepository.findMembrosMinisterioByEscalaEventoIdAndIds"
    - passo: 15
      tipo: repository
      nome: "EscalaMinisterioRepository.replaceEscalaMinisterioByEscalaEventoId"
    - passo: 16
      tipo: repository
      nome: "EscalaMinisterioRepository.areAllConfirmadosByEscalaEventoId"
    - passo: 17
      tipo: repository
      nome: "EscalaEventoRepository.updateStatusByEscalaEventoId"
    - passo: 18
      tipo: repository
      nome: "EscalaEventoRepository.findEventoIdByEscalaEventoId"
    - passo: 19
      tipo: repository
      nome: "EscalaEventoRepository.areAllConfirmadosByEventoId"
    - passo: 20
      tipo: repository
      nome: "EventoRepository.updateStatusByEventoId"
  entidades_afetadas: [EscalaMinisterio, EscalaEvento, Evento, MembroMinisterio]
  estados:
    antes: ["EscalaMinisterio anterior pode existir", "EscalaEvento status=PENDENTE ou CONFIRMADO", "Evento status=PENDENTE ou CONFIRMADO"]
    depois: ["EscalaMinisterio substituida por nova lista", "EscalaEvento recalculado para CONFIRMADO/PENDENTE", "Evento recalculado para CONFIRMADO/PENDENTE"]
  regras_aplicadas: ["lista nao pode ser nula", "idExternoMembroMinisterio deve existir e nao duplicar", "lider precisa ter vinculo com ministerio da escala", "membros da lista devem pertencer ao ministerio da escala", "membros com conflito de horario sao bloqueados", "propagacao de status deve ocorrer apos replace"]
- nome: "LOGIN_EMAIL_SENHA"
  descricao: "Autenticacao por credenciais locais com emissao de JWT."
  entrada:
    endpoint: "/api/v1/auth/login"
    metodo: "POST"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "LoginController.login"
    - passo: 2
      tipo: usecase
      nome: "LoginServiceUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "BuscarPorEmaiUseCase.execute"
    - passo: 4
      tipo: usecase
      nome: "GenerateTokenUseCase.execute"
    - passo: 5
      tipo: repository
      nome: "MembroRepository.findByEmail"
  entidades_afetadas: [Membro]
  estados:
    antes: ["Membro existente com senha hash"]
    depois: ["Nenhuma alteracao de estado persistido", "token JWT emitido"]
  regras_aplicadas: ["email deve existir", "senha informada deve casar com hash BCrypt", "credenciais invalidas geram erro"]
- nome: "LOGIN_GOOGLE"
  descricao: "Autenticacao por Google ID Token com emissao de JWT e persistencia opcional de refresh token."
  entrada:
    endpoint: "/api/v1/auth/google"
    metodo: "POST"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "GoogleAuthController.autenticarComGoogle"
    - passo: 2
      tipo: usecase
      nome: "LoginGoogleUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "AutenticarGoogleUseCase.execute"
    - passo: 4
      tipo: usecase
      nome: "AutenticarGoogleUseCase.validarAudience"
    - passo: 5
      tipo: usecase
      nome: "AutenticarGoogleUseCase.validarEmailVerificado"
    - passo: 6
      tipo: usecase
      nome: "BuscarPorEmaiUseCase.execute"
    - passo: 7
      tipo: usecase
      nome: "AtualizarSecretGoogleUseCase.execute"
    - passo: 8
      tipo: usecase
      nome: "GenerateTokenUseCase.execute"
    - passo: 9
      tipo: repository
      nome: "GoogleIdTokenVerifier.verify"
    - passo: 10
      tipo: repository
      nome: "MembroRepository.findByEmail"
    - passo: 11
      tipo: repository
      nome: "GoogleRefreshTokenMembroRepository.save"
  entidades_afetadas: [Membro, GoogleRefreshTokenMembro]
  estados:
    antes: ["idToken recebido", "Membro deve existir na base"]
    depois: ["refresh token atualizado quando informado", "token JWT emitido"]
  regras_aplicadas: ["audience do idToken deve conter clientId configurado", "email do Google deve estar verificado", "usuario deve existir na base local", "refresh token eh opcional"]
- nome: "CONFIRMACAO_ESCALA"
  descricao: "Propagacao de confirmacao da escala de membros para escala de evento e evento."
  entrada:
    endpoint: "/api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}"
    metodo: "PATCH"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "EscalaMinisterioController.salvarEscalaMinisterioPorEscalaEventoId"
    - passo: 2
      tipo: usecase
      nome: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase.execute"
    - passo: 3
      tipo: domain_service
      nome: "EscalaStatusDomainServiceImpl.recalcularStatusPorEscalaEventoId"
    - passo: 4
      tipo: repository
      nome: "EscalaMinisterioRepository.areAllConfirmadosByEscalaEventoId"
    - passo: 5
      tipo: repository
      nome: "EscalaEventoRepository.updateStatusByEscalaEventoId"
    - passo: 6
      tipo: repository
      nome: "EscalaEventoRepository.findEventoIdByEscalaEventoId"
    - passo: 7
      tipo: domain_service
      nome: "EscalaStatusDomainServiceImpl.recalcularStatusEvento"
    - passo: 8
      tipo: repository
      nome: "EscalaEventoRepository.areAllConfirmadosByEventoId"
    - passo: 9
      tipo: repository
      nome: "EventoRepository.updateStatusByEventoId"
  entidades_afetadas: [EscalaMinisterio, EscalaEvento, Evento]
  estados:
    antes: ["EscalaMinisterio com itens pendentes ou confirmados", "EscalaEvento status=PENDENTE ou CONFIRMADO", "Evento status=PENDENTE ou CONFIRMADO"]
    depois: ["EscalaEvento=CONFIRMADO quando todos EscalaMinisterio confirmados", "EscalaEvento=PENDENTE quando existir EscalaMinisterio pendente", "Evento=CONFIRMADO quando todos EscalaEvento do evento confirmados", "Evento=PENDENTE quando existir EscalaEvento pendente"]
  regras_aplicadas: ["recalculo de EscalaEvento ocorre apos salvar lista de EscalaMinisterio", "recalculo de Evento depende do status agregado de todas as EscalaEvento", "findEventoIdByEscalaEventoId nulo interrompe propagacao para Evento"]
  propagacao:
    - EscalaMinisterio -> EscalaEvento
    - EscalaEvento -> Evento
  regras:
    - quando EscalaEvento eh CONFIRMADO
    - quando Evento eh CONFIRMADO
