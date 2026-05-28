FLUXOS
- nome: "LISTAGEM_IGREJAS_CADASTRO"
  descricao: "Lista igrejas disponíveis para cadastro público."
  entrada:
    endpoint: "/api/v1/register"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "CadastroController.buscarIgrejas"
    - passo: 2
      tipo: usecase
      nome: "BuscasIgrejasUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "BuscasIgrejasUseCase.buscarIgrejas"
    - passo: 4
      tipo: repository
      nome: "IgrejaRepository.findAll"
  entidades_afetadas: [Igreja]
  estados:
    antes: ["Igrejas cadastradas"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["retorno vazio gera ObjectNotFoundException", "resultado eh mapeado para IgrejaSemiCompletoDTO"]
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
    depois: ["Membro status=ATIVO", "Membro cargo=MEMBRO"]
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
      nome: "MinisteriosRepository.findAllByIdExternoInAndIgrejaId"
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
  regras_aplicadas: ["membroDTO nao pode ser nulo", "igreja do DTO deve ser igual a igreja do token", "lider de ministerio deve ter ministerio associado", "email/cpf nao podem estar duplicados", "ministerios informados devem existir na igreja do token", "hash de senha obrigatorio"]
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
- nome: "CONSULTA_MEMBRO_DETALHE"
  descricao: "Busca dados detalhados de um membro por UUID."
  entrada:
    endpoint: "/api/v1/membros/{idExterno}"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "MembroController.buscarPorId"
    - passo: 2
      tipo: usecase
      nome: "BuscarMembroPorUUIDUseCase.execute"
    - passo: 3
      tipo: repository
      nome: "MembroRepository.findByIdExterno"
  entidades_afetadas: [Membro]
  estados:
    antes: ["Membro existente na igreja do token"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["membro deve existir", "membro deve pertencer a igreja do token", "resultado eh mapeado para MembroDetalheResponseDTO"]
- nome: "ATUALIZACAO_MEMBRO"
  descricao: "Atualiza dados cadastrais, endereço e vínculos ministeriais de um membro."
  entrada:
    endpoint: "/api/v1/membros/{idExterno}"
    metodo: "PATCH"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "MembroController.atualizarMembro"
    - passo: 2
      tipo: usecase
      nome: "AtualizarMembroUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "AtualizarMembroUseCase.atualizarEnderecoMembro"
    - passo: 4
      tipo: usecase
      nome: "AtualizarMembroUseCase.atualizarMinisterioMembro"
    - passo: 5
      tipo: repository
      nome: "MembroRepository.findByIdExternoAndIgrejaIdExterno"
    - passo: 6
      tipo: repository
      nome: "MembroMinisterioRepository.findAllByMembroIdExternoAndIgrejaIdExterno"
    - passo: 7
      tipo: repository
      nome: "EscalaMinisterioRepository.deleteByMembroMinisterioIdsAndIgrejaId"
    - passo: 8
      tipo: repository
      nome: "MembroMinisterioRepository.deleteByMembroIdExternoAndMinisterioIdExterno"
    - passo: 9
      tipo: repository
      nome: "MinisteriosRepository.findAllByIdExternoInAndIgrejaId"
    - passo: 10
      tipo: repository
      nome: "MembroMinisterioRepository.save"
    - passo: 11
      tipo: repository
      nome: "MembroRepository.save"
  entidades_afetadas: [Membro, EnderecoMembro, MembroMinisterio, Ministerio, EscalaMinisterio]
  estados:
    antes: ["Membro existente na igreja do token", "Vinculos ministeriais podem existir"]
    depois: ["Campos do membro atualizados", "Endereco atualizado quando informado", "Vinculos ministeriais substituidos quando lista informada", "Escalas associadas a vinculos removidos apagadas"]
  regras_aplicadas: ["membro deve pertencer a igreja do token", "fkIgreja nao pode divergir da igreja do token", "numero do endereco deve ser numerico", "ministerios informados devem existir na igreja do membro", "remocao de vinculo apaga escalas ministeriais associadas"]
- nome: "CONSULTA_PERFIL"
  descricao: "Busca o perfil do membro autenticado."
  entrada:
    endpoint: "/api/v1/perfil"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "PerfilController.buscarPerfil"
    - passo: 2
      tipo: usecase
      nome: "BuscarPerfilUseCase.execute"
    - passo: 3
      tipo: infrastructure
      nome: "JwtUtils.getSubject"
    - passo: 4
      tipo: infrastructure
      nome: "JwtUtils.getIgrejaId"
    - passo: 5
      tipo: repository
      nome: "MembroRepository.findByIdExterno"
  entidades_afetadas: [Membro]
  estados:
    antes: ["JWT autenticado com subject e igreja"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["perfil usa subject do JWT", "membro deve pertencer a igreja do JWT", "perfil inexistente gera ObjectNotFoundException"]
- nome: "ATUALIZACAO_PERFIL"
  descricao: "Atualiza o perfil do membro autenticado reutilizando o caso de uso de atualização de membro."
  entrada:
    endpoint: "/api/v1/perfil"
    metodo: "PATCH"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "PerfilController.atualizarPerfil"
    - passo: 2
      tipo: infrastructure
      nome: "JwtUtils.getSubject"
    - passo: 3
      tipo: infrastructure
      nome: "JwtUtils.getIgrejaId"
    - passo: 4
      tipo: usecase
      nome: "AtualizarMembroUseCase.execute"
    - passo: 5
      tipo: repository
      nome: "MembroRepository.findByIdExternoAndIgrejaIdExterno"
    - passo: 6
      tipo: repository
      nome: "MembroRepository.save"
  entidades_afetadas: [Membro, EnderecoMembro, MembroMinisterio, Ministerio, EscalaMinisterio]
  estados:
    antes: ["Membro autenticado existente na igreja do token"]
    depois: ["Perfil atualizado conforme campos enviados"]
  regras_aplicadas: ["id do membro vem do subject do JWT", "igreja vem do JWT", "mesmas regras de AtualizarMembroUseCase.execute sao aplicadas"]
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
      nome: "MinisteriosRepository.buscarMinisteriosMembro"
  entidades_afetadas: [Membro, MembroMinisterio, Ministerio]
  estados:
    antes: ["Membro autenticado com vínculo de igreja via JWT"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["consulta respeita o subject e a igreja do JWT", "retorno vazio gera ObjectNotFoundException", "resultado usa projeção direta para MinisterioSuperSimplificadoDTO"]
- nome: "CONSULTA_EVENTOS_MES_ANO"
  descricao: "Busca eventos da igreja autenticada por mes e ano."
  entrada:
    endpoint: "/api/v1/eventos"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "EventoController.buscarEventosPorMesEAno"
    - passo: 2
      tipo: usecase
      nome: "BuscarEventosPorMesEAnoUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "BuscarEventosPorMesEAnoUseCase.validarMesEAno"
    - passo: 4
      tipo: repository
      nome: "EventoRepository.findByPeriodo"
  entidades_afetadas: [Evento]
  estados:
    antes: ["Eventos existentes na igreja do token"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["mes deve estar entre 1 e 12", "ano deve ser maior que zero", "consulta restrita a igreja do token", "sem eventos no periodo gera ObjectNotFoundException"]
- nome: "CONSULTA_EVENTO_ESPECIFICO"
  descricao: "Busca detalhes de um evento por UUID."
  entrada:
    endpoint: "/api/v1/eventos/{id}"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "EventoController.buscarEventoEspecifico"
    - passo: 2
      tipo: usecase
      nome: "BuscarEventoEspecificoUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "ValidarIdExternoPreenchido.validarIdExternoPreenchido"
    - passo: 4
      tipo: repository
      nome: "EventoRepository.findByIdExterno"
  entidades_afetadas: [Evento]
  estados:
    antes: ["Evento existente"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["id do evento deve ser informado", "evento deve existir", "evento deve pertencer a igreja do token"]
- nome: "CONSULTA_ENDERECO_EVENTO"
  descricao: "Busca endereço de evento a partir do endereço da igreja autenticada."
  entrada:
    endpoint: "/api/v1/eventos/enderecos"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "EventoController.buscarEnderecoEvento"
    - passo: 2
      tipo: usecase
      nome: "BuscarEnderecoEventoUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "BuscarIgrejaPorUUIDUseCase.execute"
    - passo: 4
      tipo: repository
      nome: "IgrejaRepository.findByIdExterno"
    - passo: 5
      tipo: repository
      nome: "EnderecoEventoRepository.findByCep"
  entidades_afetadas: [Igreja, EnderecoIgreja, EnderecoEvento]
  estados:
    antes: ["Igreja autenticada existente", "EnderecoEvento cadastrado para cep/numero da igreja"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["igreja deve existir", "igreja deve possuir endereço", "endereço de evento deve existir para cep e numero da igreja"]
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
      nome: "MinisteriosRepository.findAllByIdExternoInAndIgrejaId"
    - passo: 15
      tipo: repository
      nome: "EventoRepository.save"
    - passo: 16
      tipo: repository
      nome: "EventoRepository.saveAll"
    - passo: 17
      tipo: infrastructure
      nome: "EventoProducer.publicarEventoCriadoAposCommit"
  entidades_afetadas: [Evento, EscalaEvento, EnderecoEvento, Recorrencia, Membro, Igreja, Ministerio]
  estados:
    antes: ["Evento inexistente", "recorrencia e horario ainda nao validados"]
    depois: ["Evento criado", "EscalaEvento criada para ministerios selecionados", "Eventos recorrentes criados quando aplicavel"]
  regras_aplicadas: ["recorrencia deve respeitar datas e limite de 1 ano", "horario fim deve ser maior que inicio", "evento nao pode ser no passado", "endereco deve ser completo quando idExterno nao informado", "ministerios devem pertencer a igreja do token", "evento criado publica mensagem apos commit", "organizacao depende de membro/igreja do token"]
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
      nome: "MinisteriosRepository.findAllByIdExternoInAndIgrejaId"
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
- nome: "EXCLUSAO_EVENTO_UNICO"
  descricao: "Remove apenas o evento informado."
  entrada:
    endpoint: "/api/v1/eventos/unico/{id}"
    metodo: "DELETE"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "EventoController.apagarEventoUnico"
    - passo: 2
      tipo: usecase
      nome: "ApagarEventoUnicoUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "ValidarIdExternoPreenchido.validarIdExternoPreenchido"
    - passo: 4
      tipo: repository
      nome: "EventoRepository.findByIdExterno"
    - passo: 5
      tipo: repository
      nome: "EventoRepository.deleteByIdExterno"
  entidades_afetadas: [Evento, EscalaEvento, EscalaMinisterio]
  estados:
    antes: ["Evento existente na igreja do token"]
    depois: ["Evento removido"]
  regras_aplicadas: ["id do evento deve ser informado", "evento deve pertencer a igreja do token", "deleteCount zero gera ObjectNotFoundException"]
- nome: "EXCLUSAO_EVENTOS_RECORRENTES"
  descricao: "Remove evento informado e demais eventos futuros da mesma recorrência."
  entrada:
    endpoint: "/api/v1/eventos/multiplos/{id}"
    metodo: "DELETE"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "EventoController.apagarEventosMultiplos"
    - passo: 2
      tipo: usecase
      nome: "ApagarEventosMultiplosUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "ValidarIdExternoPreenchido.validarIdExternoPreenchido"
    - passo: 4
      tipo: repository
      nome: "EventoRepository.findByIdExterno"
    - passo: 5
      tipo: repository
      nome: "EventoRepository.findByPeriodoAndRecorrencia"
    - passo: 6
      tipo: repository
      nome: "EventoRepository.deleteAll"
  entidades_afetadas: [Evento, Recorrencia, EscalaEvento, EscalaMinisterio]
  estados:
    antes: ["Evento existente na igreja do token", "Eventos recorrentes podem existir"]
    depois: ["Evento e eventos recorrentes futuros removidos"]
  regras_aplicadas: ["id do evento deve ser informado", "evento deve pertencer a igreja do token", "busca remove eventos da mesma recorrencia a partir da data inicial do evento"]
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
- nome: "DASHBOARD_MEMBROS_EVOLUCAO"
  descricao: "Busca evolução anual de membros no periodo."
  entrada:
    endpoint: "/api/v1/dashboards/membros/evolucao"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "DashboardController.buscarEvolucaoMembros"
    - passo: 2
      tipo: usecase
      nome: "BuscarKpiEvolucaoMembrosDashUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "DashboardPeriodoValidator.validarAnoInicioEFim"
    - passo: 4
      tipo: usecase
      nome: "BuscarKpiEvolucaoMembrosDashUseCase.buscarDashEvolucao"
    - passo: 5
      tipo: repository
      nome: "MembroRepository.buscarMembrosPorAno"
  entidades_afetadas: [Membro]
  estados:
    antes: ["Dados historicos de membros existentes"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["anoInicio e anoFim devem ser validos", "consulta restrita a igreja do token", "resultado vazio gera ObjectNotFoundException"]
- nome: "DASHBOARD_MEMBROS_FAIXA_ETARIA"
  descricao: "Busca distribuição de membros por faixa etária."
  entrada:
    endpoint: "/api/v1/dashboards/membros/faixa-etaria"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "DashboardController.buscarFaixaEtariaMembros"
    - passo: 2
      tipo: usecase
      nome: "BuscarKpiFaixaEtariaMembrosDashUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "DashboardPeriodoValidator.validarAnoInicioEFim"
    - passo: 4
      tipo: usecase
      nome: "BuscarKpiFaixaEtariaMembrosDashUseCase.buscarDashFaixaEtaria"
    - passo: 5
      tipo: repository
      nome: "MembroRepository.buscarMembrosPorFaixaEtaria"
  entidades_afetadas: [Membro]
  estados:
    antes: ["Dados de nascimento de membros existentes"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["anoInicio e anoFim devem ser validos", "consulta restrita a igreja do token", "resultado vazio gera ObjectNotFoundException"]
- nome: "DASHBOARD_MEMBROS_GENERO"
  descricao: "Busca distribuição de membros por gênero."
  entrada:
    endpoint: "/api/v1/dashboards/membros/genero"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "DashboardController.buscarGeneroMembros"
    - passo: 2
      tipo: usecase
      nome: "BuscarKpiGeneroMembrosDashUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "DashboardPeriodoValidator.validarAnoInicioEFim"
    - passo: 4
      tipo: usecase
      nome: "BuscarKpiGeneroMembrosDashUseCase.buscarDashGenero"
    - passo: 5
      tipo: repository
      nome: "MembroRepository.buscarMembrosPorGenero"
  entidades_afetadas: [Membro]
  estados:
    antes: ["Dados de genero de membros existentes"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["anoInicio e anoFim devem ser validos", "consulta restrita a igreja do token", "resultado vazio gera ObjectNotFoundException"]
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
- nome: "DASHBOARD_MINISTERIOS_EVOLUCAO"
  descricao: "Busca evolução de membros de um ministério no periodo."
  entrada:
    endpoint: "/api/v1/dashboards/ministerios/evolucao/{idMinisterio}"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "DashboardController.buscarEvolucaoMinisterio"
    - passo: 2
      tipo: usecase
      nome: "BuscarKpiEvolucaoMinisteriosDashUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "DashboardPeriodoValidator.validarAnoInicioEFim"
    - passo: 4
      tipo: usecase
      nome: "BuscarKpiEvolucaoMinisteriosDashUseCase.ministerioBuscarDashEvolucao"
    - passo: 5
      tipo: repository
      nome: "MembroMinisterioRepository.buscarDashEvolucaoUmAno"
    - passo: 6
      tipo: repository
      nome: "MembroMinisterioRepository.buscarDashEvolucaoPeriodo"
  entidades_afetadas: [MembroMinisterio, Ministerio]
  estados:
    antes: ["Historico de membros do ministerio existente"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["idMinisterio deve ser informado", "anoInicio e anoFim devem ser validos", "consulta restrita a igreja do token", "ano unico usa consulta especifica de um ano"]
- nome: "DASHBOARD_MINISTERIOS_QUANTIDADE_MEMBROS"
  descricao: "Busca quantidade de membros por ministério no periodo."
  entrada:
    endpoint: "/api/v1/dashboards/ministerios/quantidade-membros"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "DashboardController.buscarQuantidadeMembrosPorMinisterio"
    - passo: 2
      tipo: usecase
      nome: "BuscarKpiQuantidadeMembrosPorMinisterioDashUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "DashboardPeriodoValidator.validarAnoInicioEFim"
    - passo: 4
      tipo: usecase
      nome: "BuscarKpiQuantidadeMembrosPorMinisterioDashUseCase.ministerioBuscarDashQuantidadeMembro"
    - passo: 5
      tipo: repository
      nome: "MembroMinisterioRepository.buscarQuantidadeMembros"
  entidades_afetadas: [MembroMinisterio, Ministerio]
  estados:
    antes: ["Vinculos de membros em ministerios existentes"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["anoInicio e anoFim devem ser validos", "consulta restrita a igreja do token", "resultado vazio gera ObjectNotFoundException"]
- nome: "DASHBOARD_MINISTERIOS_QUANTIDADE_EVENTOS"
  descricao: "Busca quantidade de eventos por ministério no periodo."
  entrada:
    endpoint: "/api/v1/dashboards/ministerios/quantidade-eventos"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "DashboardController.buscarQuantidadeEventosPorMinisterio"
    - passo: 2
      tipo: usecase
      nome: "buscarKpiQuantidadeEventosPorMinisterioDashUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "DashboardPeriodoValidator.validarAnoInicioEFim"
    - passo: 4
      tipo: usecase
      nome: "buscarKpiQuantidadeEventosPorMinisterioDashUseCase.ministerioBuscarDashQuantidadeEventos"
    - passo: 5
      tipo: repository
      nome: "EventoRepository.contarEventosPorMinisterioNoPeriodo"
  entidades_afetadas: [Evento, Ministerio]
  estados:
    antes: ["Eventos com ministerios existentes"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["anoInicio e anoFim devem ser validos", "consulta restrita a igreja do token", "resultado vazio gera ObjectNotFoundException"]
- nome: "CONSULTA_MINISTERIOS_GERAIS"
  descricao: "Lista ministérios ativos para uso geral dentro da igreja do token."
  entrada:
    endpoint: "/api/v1/ministerios"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "MinisteriosController.buscarMinisteriosGerais"
    - passo: 2
      tipo: usecase
      nome: "BuscarMinisteriosGeraisUseCase.execute"
    - passo: 3
      tipo: repository
      nome: "MinisteriosRepository.findByIgrejaIdExterno"
  entidades_afetadas: [Ministerio]
  estados:
    antes: ["Ministerios existentes na igreja do token"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["consulta restrita a igreja do token", "retorno vazio gera ObjectNotFoundException", "resultado eh mapeado para MinisterioSimplificadoDTO"]
- nome: "CONSULTA_MINISTERIOS_GOVERNO"
  descricao: "Busca paginada de ministérios para governo com ou sem filtro."
  entrada:
    endpoint: "/api/v1/ministerios/governo"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "MinisteriosController.buscarMinisteriosGoverno"
    - passo: 2
      tipo: usecase
      nome: "BuscarMinisteriosGovernoSemFiltroUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "BuscarMinisteriosGovernoComFiltroUseCase.execute"
    - passo: 4
      tipo: repository
      nome: "MinisteriosRepository.findByIgrejaIdExterno"
    - passo: 5
      tipo: repository
      nome: "MinisteriosRepository.buscarComFiltros"
  entidades_afetadas: [Ministerio]
  estados:
    antes: ["Ministerios existentes na igreja do token"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["sem busca e sem status chama fluxo sem filtro", "com busca ou status chama fluxo com filtro", "consulta restrita a igreja do token", "retorno vazio gera ObjectNotFoundException"]
- nome: "CRIACAO_MINISTERIO"
  descricao: "Cria ministério e vincula o líder informado."
  entrada:
    endpoint: "/api/v1/ministerios/governo"
    metodo: "POST"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "MinisteriosController.adicionarMinisterio"
    - passo: 2
      tipo: usecase
      nome: "AdicionarMinisterioUseCase.execute"
    - passo: 3
      tipo: repository
      nome: "MembroJpaRepository.findByIdExterno"
    - passo: 4
      tipo: repository
      nome: "MinisteriosRepository.save"
  entidades_afetadas: [Ministerio, Membro, MembroMinisterio]
  estados:
    antes: ["Lider existente na igreja do token"]
    depois: ["Ministerio criado com status=ATIVO", "Membro promovido para LIDER_MINISTERIO quando necessario", "Vinculo MembroMinisterio criado como LIDER_MINISTERIO"]
  regras_aplicadas: ["lider deve existir", "lider deve pertencer a igreja do token", "membro lider eh promovido quando cargo diferente de LIDER_MINISTERIO"]
- nome: "ATUALIZACAO_MINISTERIO"
  descricao: "Atualiza nome, status e líder de um ministério."
  entrada:
    endpoint: "/api/v1/ministerios/governo/{idMinisterio}"
    metodo: "PATCH"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "MinisteriosController.editarMinisterio"
    - passo: 2
      tipo: usecase
      nome: "EditarMinisterioUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "EditarMinisterioUseCase.atualizarLider"
    - passo: 4
      tipo: repository
      nome: "MinisteriosRepository.findByIdExterno"
    - passo: 5
      tipo: repository
      nome: "MembroJpaRepository.findByIdExterno"
    - passo: 6
      tipo: repository
      nome: "MinisteriosRepository.save"
  entidades_afetadas: [Ministerio, Membro, MembroMinisterio]
  estados:
    antes: ["Ministerio existente na igreja do token", "Novo lider opcional"]
    depois: ["Ministerio atualizado", "Lider antigo pode voltar para MEMBRO", "Novo lider vinculado ou promovido"]
  regras_aplicadas: ["ministerio deve existir na igreja do token", "novo lider deve existir na igreja do token", "troca de lider atualiza cargo no vínculo ministerial", "lider antigo so volta a MEMBRO se nao liderar outro ministerio"]
- nome: "CONSULTA_MEMBROS_MINISTERIO_LIDER"
  descricao: "Busca membros de um ministério para visão de líder com ou sem filtro."
  entrada:
    endpoint: "/api/v1/ministerios/lider-ministerio/{idMinisterio}"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "MinisteriosController.buscarMembroMinisterioLiderMinisterio"
    - passo: 2
      tipo: usecase
      nome: "BuscarMembroMinisterioLiderMinisterioSemFiltroUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "BuscarMembroMinisterioLiderMinisterioComFiltroUseCase.execute"
    - passo: 4
      tipo: repository
      nome: "MembroMinisterioRepository.buscarPorMembroMinisterioSemFiltro"
    - passo: 5
      tipo: repository
      nome: "MembroMinisterioRepository.buscarPorMembroMinisterioComFiltro"
  entidades_afetadas: [MembroMinisterio, Membro, Ministerio]
  estados:
    antes: ["Membros vinculados ao ministerio"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["sem busca e sem status chama fluxo sem filtro", "com busca ou status chama fluxo com filtro", "retorno vazio gera ObjectNotFoundException"]
- nome: "CONSULTA_MINISTERIOS_LIDER"
  descricao: "Lista ministérios liderados pelo membro autenticado."
  entrada:
    endpoint: "/api/v1/ministerios/lider-ministerio"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "MinisteriosController.buscarMinisteriosLiderMinisterio"
    - passo: 2
      tipo: usecase
      nome: "BuscarMinisteriosLiderMinisterioUseCase.execute"
    - passo: 3
      tipo: repository
      nome: "MembroMinisterioRepository.buscarMinisterioLider"
  entidades_afetadas: [MembroMinisterio, Ministerio, Membro]
  estados:
    antes: ["Membro autenticado pode liderar ministerios"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["consulta usa subject e igreja do JWT", "retorno vazio gera ObjectNotFoundException"]
- nome: "ADICIONAR_MEMBRO_MINISTERIO"
  descricao: "Adiciona membro a um ministério pela visão de líder."
  entrada:
    endpoint: "/api/v1/ministerios/lider-ministerio/{idMinisterio}"
    metodo: "PATCH"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "MinisteriosController.adicionarMembroMinisterioLiderMinisterio"
    - passo: 2
      tipo: usecase
      nome: "AdicionarMembroMinisterioLiderMinisterioUseCase.execute"
    - passo: 3
      tipo: repository
      nome: "MinisteriosRepository.buscarIdPorUUID"
    - passo: 4
      tipo: repository
      nome: "MinisteriosRepository.findByIdExternoAndIgrejaId"
    - passo: 5
      tipo: repository
      nome: "MembroMinisterioRepository.buscarMinisterioLider"
    - passo: 6
      tipo: repository
      nome: "MembroJpaRepository.findByIdExterno"
    - passo: 7
      tipo: repository
      nome: "MembroMinisterioRepository.save"
  entidades_afetadas: [MembroMinisterio, Membro, Ministerio]
  estados:
    antes: ["Ministerio existente", "Membro alvo existente na igreja do token", "Executor lidera o ministerio"]
    depois: ["MembroMinisterio criado com cargo MEMBRO_MINISTERIO"]
  regras_aplicadas: ["dto nao pode ser nulo", "ministerio deve existir na igreja do token", "executor deve liderar o ministerio", "membro alvo deve existir na igreja do token"]
- nome: "REMOVER_MEMBRO_MINISTERIO"
  descricao: "Remove membro de um ministério pela visão de líder."
  entrada:
    endpoint: "/api/v1/ministerios/lider-ministerio/{idMinisterio}/{idMembroMinisterio}"
    metodo: "DELETE"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "MinisteriosController.removerMembroMinisterioLiderMinisterio"
    - passo: 2
      tipo: usecase
      nome: "RemoverMembroMinisterioLiderMinisterioUseCase.execute"
    - passo: 3
      tipo: repository
      nome: "MinisteriosRepository.findByIdExterno"
    - passo: 4
      tipo: repository
      nome: "MembroMinisterioRepository.deleteByMembroIdExternoAndMinisterioIdExterno"
  entidades_afetadas: [MembroMinisterio, Ministerio]
  estados:
    antes: ["Ministerio existente na igreja do token", "Vinculo MembroMinisterio existente"]
    depois: ["Vinculo MembroMinisterio removido"]
  regras_aplicadas: ["ministerio deve existir na igreja do token", "deleteCount zero gera ObjectNotFoundException"]
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
- nome: "CONSULTA_ESCALA_EVENTO_ESCALADA"
  descricao: "Consulta ministérios escalados para um evento."
  entrada:
    endpoint: "/api/v1/escalas-evento/governo/{eventoId}"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "EscalaEventoController.buscarEscalaEventoEscaladoPorEventoId"
    - passo: 2
      tipo: usecase
      nome: "BuscarEscalaEventoEscaladoPorEventoIdUseCase.execute"
    - passo: 3
      tipo: repository
      nome: "EscalaEventoRepository.findEscalaEventoEscaladoByEventoId"
  entidades_afetadas: [EscalaEvento, Evento, Ministerio]
  estados:
    antes: ["Escalas de evento existentes para evento informado"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["eventoId deve ser informado", "consulta restrita a igreja do token"]
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
      nome: "MinisteriosRepository.findAllByIdExternoInAndIgrejaId"
    - passo: 9
      tipo: repository
      nome: "EscalaMinisterioRepository.deleteByEscalaEventoIdAndIgrejaId"
    - passo: 10
      tipo: repository
      nome: "EventoRepository.save"
    - passo: 11
      tipo: repository
      nome: "EscalaEventoRepository.areAllConfirmadosByEventoId"
    - passo: 12
      tipo: repository
      nome: "EventoRepository.updateStatusByEventoId"
  entidades_afetadas: [Evento, EscalaEvento, Ministerio]
  estados:
    antes: ["Evento e escalas existentes", "EscalaEvento possivelmente PENDENTE"]
    depois: ["Escalas do evento substituidas/atualizadas", "Status do Evento recalculado"]
  regras_aplicadas: ["lista de escala nao pode ser vazia", "evento deve pertencer a igreja do token", "somente ministerios marcados como escalados entram no recalculo", "status final do evento depende de confirmacao total"]
- nome: "CONSULTA_ESCALA_MINISTERIO_CONSOLIDADA"
  descricao: "Consulta escalas de ministério para visão de líder por mes/ano com filtros opcionais."
  entrada:
    endpoint: "/api/v1/escalas-ministerio/lider-ministerio"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "EscalaMinisterioController.buscarEscalaMinisterioConsolidadoPorMesAno"
    - passo: 2
      tipo: usecase
      nome: "BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "ValidarMesEAno.validarMesEAno"
    - passo: 4
      tipo: repository
      nome: "MembroMinisterioRepository.buscarMinisterioLider"
    - passo: 5
      tipo: repository
      nome: "EscalaMinisterioRepository.findEscalaMinisterioConsolidadoByPeriodo"
  entidades_afetadas: [EscalaMinisterio, EscalaEvento, Evento, Ministerio, MembroMinisterio]
  estados:
    antes: ["Membro autenticado lidera um ou mais ministerios", "Escalas existentes no periodo"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["mes e ano devem ser validos", "lider deve possuir ministerios vinculados", "ministerioId informado deve estar entre ministerios liderados", "consulta restrita a igreja do token"]
- nome: "CONSULTA_ESCALA_MINISTERIO_MEMBRO"
  descricao: "Consulta escalas de ministério do membro autenticado por mes/ano."
  entrada:
    endpoint: "/api/v1/escalas-ministerio/membro"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "EscalaMinisterioController.buscarEscalaMinisterioPorMembroIdMesAno"
    - passo: 2
      tipo: usecase
      nome: "BuscarEscalaMinisterioPorMembroIdMesAnoUseCase.execute"
    - passo: 3
      tipo: usecase
      nome: "ValidarMesEAno.validarMesEAno"
    - passo: 4
      tipo: repository
      nome: "MembroMinisterioRepository.buscarMembro"
    - passo: 5
      tipo: repository
      nome: "EscalaMinisterioRepository.findEscalaMinisterioByPeriodo"
  entidades_afetadas: [EscalaMinisterio, MembroMinisterio, Ministerio, Evento]
  estados:
    antes: ["Membro autenticado pode possuir vinculos ministeriais", "Escalas existentes no periodo"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["mes e ano devem ser validos", "membro deve possuir ministerios vinculados", "ministerioId opcional deve pertencer ao membro", "consulta restrita a igreja do token"]
- nome: "CONSULTA_MEMBROS_DISPONIVEIS_ESCALA_MINISTERIO"
  descricao: "Conta membros disponíveis para escala de ministério."
  entrada:
    endpoint: "/api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}/membros-disponiveis"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "EscalaMinisterioController.buscarMembrosMinisterioDisponiveisPorEscalaEventoId"
    - passo: 2
      tipo: usecase
      nome: "BuscarMembrosMinisterioDisponiveisPorEscalaEventoIdUseCase.execute"
    - passo: 3
      tipo: repository
      nome: "EscalaEventoRepository.findMinisterioIdByEscalaEventoId"
    - passo: 4
      tipo: repository
      nome: "MembroMinisterioRepository.buscarMinisterioLider"
    - passo: 5
      tipo: repository
      nome: "EscalaMinisterioRepository.findEscalaMembroMinisterioByEscalaEventoId"
    - passo: 6
      tipo: repository
      nome: "EscalaMinisterioRepository.findMembrosMinisterioOcupadosByEscalaEventoId"
  entidades_afetadas: [EscalaMinisterio, EscalaEvento, MembroMinisterio]
  estados:
    antes: ["EscalaEvento existente", "Membros do ministerio existentes"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["escalaEventoId deve pertencer a igreja do token", "executor deve liderar ministerio da escala", "membros ocupados em conflito nao contam como disponiveis"]
- nome: "CONSULTA_MEMBROS_ESCALA_MINISTERIO"
  descricao: "Lista membros do ministério para uma escala de evento."
  entrada:
    endpoint: "/api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "EscalaMinisterioController.buscarMembrosMinisterioPorEscalaEventoId"
    - passo: 2
      tipo: usecase
      nome: "BuscarMembrosMinisterioPorEscalaEventoIdUseCase.execute"
    - passo: 3
      tipo: repository
      nome: "EscalaEventoRepository.findMinisterioIdByEscalaEventoId"
    - passo: 4
      tipo: repository
      nome: "MembroMinisterioRepository.buscarMinisterioLider"
    - passo: 5
      tipo: repository
      nome: "EscalaMinisterioRepository.findEscalaMembroMinisterioByEscalaEventoId"
    - passo: 6
      tipo: repository
      nome: "EscalaMinisterioRepository.findMembrosMinisterioOcupadosByEscalaEventoId"
  entidades_afetadas: [EscalaMinisterio, EscalaEvento, MembroMinisterio]
  estados:
    antes: ["EscalaEvento existente", "Membros do ministerio existentes"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["escalaEventoId deve pertencer a igreja do token", "executor deve liderar ministerio da escala", "retorno indica membros ocupados quando houver conflito de horario"]
- nome: "RANDOMIZACAO_MEMBROS_ESCALA_MINISTERIO"
  descricao: "Seleciona membros aleatórios disponíveis para uma escala de ministério."
  entrada:
    endpoint: "/api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}/{quantidadeMembrosRandomizados}"
    metodo: "GET"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "EscalaMinisterioController.buscarMembrosMinisterioRandomizadosPorEscalaEventoId"
    - passo: 2
      tipo: usecase
      nome: "BuscarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase.execute"
    - passo: 3
      tipo: repository
      nome: "EscalaEventoRepository.findMinisterioIdByEscalaEventoId"
    - passo: 4
      tipo: repository
      nome: "MembroMinisterioRepository.buscarMinisterioLider"
    - passo: 5
      tipo: repository
      nome: "EscalaMinisterioRepository.findEscalaMembroMinisterioByEscalaEventoId"
    - passo: 6
      tipo: repository
      nome: "EscalaMinisterioRepository.findMembrosMinisterioOcupadosByEscalaEventoId"
  entidades_afetadas: [EscalaMinisterio, EscalaEvento, MembroMinisterio]
  estados:
    antes: ["Membros disponiveis para escala"]
    depois: ["Nenhuma alteracao de estado"]
  regras_aplicadas: ["quantidade deve ser positiva", "executor deve liderar ministerio da escala", "membros ocupados sao excluidos da randomizacao", "quantidade solicitada nao pode exceder membros disponiveis"]
- nome: "REVISAO_RANDOMIZACAO_ESCALA_MINISTERIO"
  descricao: "Troca um membro randomizado por outro membro disponível."
  entrada:
    endpoint: "/api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}/revisar-randomizacao/{membroMinisterioIdASerTrocado}"
    metodo: "POST"
  sequencia:
    - passo: 1
      tipo: controller
      nome: "EscalaMinisterioController.RevisarMembrosMinisterioRandomizadosPorEscalaEventoId"
    - passo: 2
      tipo: usecase
      nome: "RevisarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase.execute"
    - passo: 3
      tipo: repository
      nome: "EscalaEventoRepository.findMinisterioIdByEscalaEventoId"
    - passo: 4
      tipo: repository
      nome: "MembroMinisterioRepository.buscarMinisterioLider"
    - passo: 5
      tipo: repository
      nome: "EscalaMinisterioRepository.findEscalaMembroMinisterioByEscalaEventoId"
    - passo: 6
      tipo: repository
      nome: "EscalaMinisterioRepository.findMembrosMinisterioOcupadosByEscalaEventoId"
  entidades_afetadas: [EscalaMinisterio, EscalaEvento, MembroMinisterio]
  estados:
    antes: ["Lista de membros randomizados selecionada", "Membro a ser trocado informado"]
    depois: ["Lista randomizada revisada sem persistencia"]
  regras_aplicadas: ["executor deve liderar ministerio da escala", "membro a ser trocado deve estar na lista selecionada", "novo membro deve estar disponivel e nao selecionado"]
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
  descricao: "Autenticacao por authorization code do Google com emissao de JWT e persistencia opcional de refresh token."
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
      tipo: infrastructure
      nome: "GoogleAuthorizationCodeExchanger.exchange"
    - passo: 4
      tipo: usecase
      nome: "AutenticarGoogleUseCase.execute"
    - passo: 5
      tipo: usecase
      nome: "AutenticarGoogleUseCase.validarAudience"
    - passo: 6
      tipo: usecase
      nome: "AutenticarGoogleUseCase.validarEmailVerificado"
    - passo: 7
      tipo: usecase
      nome: "BuscarPorEmaiUseCase.execute"
    - passo: 8
      tipo: usecase
      nome: "AtualizarSecretGoogleUseCase.execute"
    - passo: 9
      tipo: usecase
      nome: "GenerateTokenUseCase.execute"
    - passo: 10
      tipo: infrastructure
      nome: "GoogleIdTokenVerifier.verify"
    - passo: 11
      tipo: repository
      nome: "MembroRepository.findByEmail"
    - passo: 12
      tipo: repository
      nome: "GoogleRefreshTokenMembroRepository.findByMembroId"
    - passo: 13
      tipo: repository
      nome: "GoogleRefreshTokenMembroRepository.save"
  entidades_afetadas: [Membro, GoogleRefreshTokenMembro]
  estados:
    antes: ["authorizationCode recebido", "Membro deve existir na base"]
    depois: ["idToken retornado pelo Google validado", "refresh token atualizado quando retornado pelo Google", "token JWT emitido"]
  regras_aplicadas: ["authorization code deve ser trocado por id_token no Google", "resposta do Google deve conter id_token", "audience do idToken deve conter clientId configurado", "email do Google deve estar verificado", "usuario deve existir na base local", "refresh token retornado pelo Google eh opcional", "refresh token existente deve pertencer a mesma igreja"]
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
