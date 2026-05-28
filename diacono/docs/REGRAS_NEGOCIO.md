REGRAS

- id: REGRA_001
  nome: "Validacao de cadastro nao nulo"
  descricao: "Dados do membro para cadastro nao podem ser nulos."
  entidade: "Membro"
  condicao: "membroDTO == null"
  acao: "lanca ObjectSaveErrorException"
  origem_codigo:
    classe: "CadastrarMembroUseCase"
    metodo: "criarMembroExterno"
  dependencias: []

- id: REGRA_002
  nome: "Validacao de email e CPF duplicados"
  descricao: "Email ou CPF nao podem estar duplicados na base."
  entidade: "Membro"
  condicao: "findByEmailOrCpf(email, cpf).isPresent()"
  acao: "lanca ObjectExistsException"
  origem_codigo:
    classe: "CadastrarMembroUseCase"
    metodo: "criarMembroExterno"
  dependencias: ["REGRA_001"]

- id: REGRA_003
  nome: "Hash de senha obrigatorio"
  descricao: "Senha deve ser hash antes de persistir na base."
  entidade: "Membro"
  condicao: "senha em texto plano"
  acao: "aplica BCryptPasswordEncoder.encode()"
  origem_codigo:
    classe: "ValidarCriacaoMembro"
    metodo: "hashSenha"
  dependencias: []

- id: REGRA_004
  nome: "Status inicial de membro"
  descricao: "Novo membro cadastrado tem status ATIVO."
  entidade: "Membro"
  condicao: "membro criado"
  acao: "define status=ATIVO"
  origem_codigo:
    classe: "CadastrarMembroUseCase"
    metodo: "criarMembroExterno"
  dependencias: []

- id: REGRA_005
  nome: "Cargo inicial de membro externo"
  descricao: "Membro cadastrado externamente tem cargo MEMBRO."
  entidade: "Membro"
  condicao: "membro cadastrado via endpoint publico"
  acao: "define cargo=MEMBRO"
  origem_codigo:
    classe: "CadastrarMembroUseCase"
    metodo: "criarMembroExterno"
  dependencias: ["REGRA_004"]

- id: REGRA_006
  nome: "Cargo de membro interno conforme DTO"
  descricao: "Membro cadastrado internamente recebe o cargo informado no DTO."
  entidade: "Membro"
  condicao: "membroDTO.cargo() informado"
  acao: "define cargoMembro conforme DTO"
  origem_codigo:
    classe: "CriarMembroUseCase"
    metodo: "criarMembroSemMinisterio"
  dependencias: []

- id: REGRA_007
  nome: "Lider de ministerio deve ter ministerio"
  descricao: "Se cargo eh LIDER_MINISTERIO, deve ter idExternoMinisterios preenchido."
  entidade: "Membro"
  condicao: "cargo == LIDER_MINISTERIO && (idExternoMinisterios == null || idExternoMinisterios.isEmpty())"
  acao: "lanca ObjectSaveErrorException"
  origem_codigo:
    classe: "CriarMembroUseCase"
    metodo: "criarMembroSemMinisterio"
  dependencias: ["REGRA_006"]

- id: REGRA_008
  nome: "Validacao de recorrencia de evento"
  descricao: "Evento com recorrencia deve ter dataInicio e dataFim preenchidas e fim maior que inicio."
  entidade: "Evento"
  condicao: "tipoRecorrencia != NAO_REPETE && (dataTerminoRecorrencia == null || dataInicioRecorrencia == null || dataTerminoRecorrencia <= dataInicioRecorrencia)"
  acao: "lanca FieldInvalidException"
  origem_codigo:
    classe: "CriarEventoUseCase"
    metodo: "validarRecorrencia"
  dependencias: []

- id: REGRA_009
  nome: "Limite de recorrencia de 1 ano"
  descricao: "Evento recorrente nao pode ultrapassar 1 ano de duracao."
  entidade: "Evento"
  condicao: "dataInicioRecorrencia.plusDays(365).isBefore(dataTerminoRecorrencia)"
  acao: "lanca FieldInvalidException"
  origem_codigo:
    classe: "CriarEventoUseCase"
    metodo: "validarRecorrencia"
  dependencias: ["REGRA_008"]

- id: REGRA_010
  nome: "Data de recorrencia igual a data do evento"
  descricao: "Data de inicio da recorrencia deve ser igual a data de inicio do evento."
  entidade: "Evento"
  condicao: "!dataInicioRecorrencia.isEqual(dataHoraInicio.toLocalDate())"
  acao: "lanca FieldInvalidException"
  origem_codigo:
    classe: "CriarEventoUseCase"
    metodo: "validarRecorrencia"
  dependencias: ["REGRA_008"]

- id: REGRA_011
  nome: "Validacao de horario de evento"
  descricao: "Horario de fim do evento nao pode ser anterior ao horario de inicio."
  entidade: "Evento"
  condicao: "dataHoraFim.isBefore(dataHoraInicio)"
  acao: "lanca TimeInvalidException"
  origem_codigo:
    classe: "ValidarHora"
    metodo: "validaHoraInicioMenorHoraFim"
  dependencias: []

- id: REGRA_012
  nome: "Evento nao pode ser no passado"
  descricao: "Evento nao pode ser agendado para horario passado."
  entidade: "Evento"
  condicao: "dataHoraInicio.isBefore(now.plusMinutes(1)) && dataHoraFim.isBefore(now.plusMinutes(1))"
  acao: "lanca TimeInvalidException"
  origem_codigo:
    classe: "ValidarHora"
    metodo: "validarHoraFuturo"
  dependencias: []

- id: REGRA_013
  nome: "Endereco completo para evento novo"
  descricao: "Endereco do evento deve estar completo quando idExterno nao informado."
  entidade: "EnderecoEvento"
  condicao: "idExterno == null && (cep == null || estado == null || cidade == null || bairro == null || rua == null || numero == null || apelido == null)"
  acao: "lanca FieldInvalidException"
  origem_codigo:
    classe: "CriarEventoUseCase"
    metodo: "validarEnderecoEvento"
  dependencias: []

- id: REGRA_014
  nome: "Gerador de escala de evento na criacao"
  descricao: "Escala de evento eh gerada automaticamente ao criar evento."
  entidade: "EscalaEvento"
  condicao: "evento sem recorrencia criado"
  acao: "chama GerarEscalaEventoUseCase.executeParaCriacao()"
  origem_codigo:
    classe: "CriarEventoUseCase"
    metodo: "criarEventoSemRecorrencia"
  dependencias: []

- id: REGRA_015
  nome: "Atualizacao de escala de evento"
  descricao: "Escala de evento eh atualizada quando fkMinisterios eh alterado."
  entidade: "EscalaEvento"
  condicao: "fkMinisterios != null && !fkMinisterios.isEmpty()"
  acao: "chama GerarEscalaEventoUseCase.executeParaAtualizacao()"
  origem_codigo:
    classe: "AtualizarEventoUseCase"
    metodo: "execute"
  dependencias: []

- id: REGRA_016
  nome: "Validacao de lista de escala evento nao vazia"
  descricao: "Lista de escalas do evento nao pode estar vazia na atualizacao."
  entidade: "EscalaEvento"
  condicao: "listaEscalaEvento == null || listaEscalaEvento.isEmpty()"
  acao: "lanca FieldInvalidException"
  origem_codigo:
    classe: "AtualizarEscalaEventoPorEventoIdUseCase"
    metodo: "validarListaEscalaEvento"
  dependencias: []

- id: REGRA_017
  nome: "Validacao de request de escala ministerio nao nulo"
  descricao: "Lista de escala do ministerio nao pode ser nula na persistencia."
  entidade: "EscalaMinisterio"
  condicao: "escalasMinisterio == null"
  acao: "lanca FieldInvalidException"
  origem_codigo:
    classe: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase"
    metodo: "validarRequest"
  dependencias: []

- id: REGRA_018
  nome: "Validacao de ID do membro ministerio obrigatorio"
  descricao: "Cada item da escala do ministerio deve ter idExternoMembroMinisterio preenchido."
  entidade: "EscalaMinisterio"
  condicao: "item.idExternoMembroMinisterio() == null"
  acao: "lanca FieldInvalidException"
  origem_codigo:
    classe: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase"
    metodo: "validarRequest"
  dependencias: ["REGRA_017"]

- id: REGRA_019
  nome: "Validacao de membros duplicados na escala"
  descricao: "A lista de escalados nao pode conter membros duplicados."
  entidade: "EscalaMinisterio"
  condicao: "totalMembros != membrosDistintos"
  acao: "lanca FieldInvalidException"
  origem_codigo:
    classe: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase"
    metodo: "validarRequest"
  dependencias: ["REGRA_018"]

- id: REGRA_020
  nome: "Validacao de vinculo lider ministerio"
  descricao: "Lider que realiza escala deve ter vinculo com o ministerio da escala."
  entidade: "MembroMinisterio"
  condicao: "!listaMinisteriosLider.contains(ministerioId)"
  acao: "lanca ObjectNotFoundException"
  origem_codigo:
    classe: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase"
    metodo: "validarEscalaEventoId"
  dependencias: []

- id: REGRA_021
  nome: "Validacao de membros pertencerem ao ministerio"
  descricao: "Todos os membros escalados devem pertencer ao ministerio da escala."
  entidade: "EscalaMinisterio"
  condicao: "idMembroMinisterio nao existe em idsMembrosDisponiveis"
  acao: "lanca FieldInvalidException"
  origem_codigo:
    classe: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase"
    metodo: "validarMembrosDaEscala"
  dependencias: []

- id: REGRA_022
  nome: "Validacao de conflito de escala"
  descricao: "Membros com conflito de horario sao bloqueados na escala."
  entidade: "EscalaMinisterio"
  condicao: "membrosOcupados.contains(idMembroMinisterio)"
  acao: "lanca FieldInvalidException"
  origem_codigo:
    classe: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase"
    metodo: "validarConflitoDeEscala"
  dependencias: []

- id: REGRA_023
  nome: "confirmacao_escala_evento"
  descricao: "EscalaEvento eh marcada CONFIRMADO quando todos os EscalaMinisterio sao CONFIRMADO."
  entidade: "EscalaEvento"
  condicao: "escalaMinisterioRepository.areAllConfirmadosByEscalaEventoId(escalaEventoId) == true"
  acao: "escalaEventoRepository.updateStatusByEscalaEventoId(CONFIRMADO)"
  origem_codigo:
    classe: "EscalaStatusDomainServiceImpl"
    metodo: "recalcularStatusPorEscalaEventoId"
  dependencias: []

- id: REGRA_024
  nome: "Pendencia de escala evento"
  descricao: "EscalaEvento permanece PENDENTE enquanto existir EscalaMinisterio PENDENTE."
  entidade: "EscalaEvento"
  condicao: "escalaMinisterioRepository.areAllConfirmadosByEscalaEventoId(escalaEventoId) == false"
  acao: "escalaEventoRepository.updateStatusByEscalaEventoId(PENDENTE)"
  origem_codigo:
    classe: "EscalaStatusDomainServiceImpl"
    metodo: "recalcularStatusPorEscalaEventoId"
  dependencias: ["REGRA_023"]

- id: REGRA_025
  nome: "confirmacao_evento"
  descricao: "Evento eh marcado CONFIRMADO quando todas as EscalaEvento sao CONFIRMADO."
  entidade: "Evento"
  condicao: "escalaEventoRepository.areAllConfirmadosByEventoId(eventoId) == true"
  acao: "eventoRepository.updateStatusByEventoId(CONFIRMADO)"
  origem_codigo:
    classe: "EscalaStatusDomainServiceImpl"
    metodo: "recalcularStatusEvento"
  dependencias: []

- id: REGRA_026
  nome: "Pendencia de evento"
  descricao: "Evento permanece PENDENTE enquanto existir EscalaEvento PENDENTE."
  entidade: "Evento"
  condicao: "escalaEventoRepository.areAllConfirmadosByEventoId(eventoId) == false"
  acao: "eventoRepository.updateStatusByEventoId(PENDENTE)"
  origem_codigo:
    classe: "EscalaStatusDomainServiceImpl"
    metodo: "recalcularStatusEvento"
  dependencias: ["REGRA_025"]

- id: REGRA_027
  nome: "Propagacao de confirmacao escala para evento"
  descricao: "Quando EscalaEvento muda de status, Evento eh recalculado."
  entidade: "Evento"
  condicao: "recalcularStatusPorEscalaEventoId chamado && eventoId extraido de escalaEventoId"
  acao: "chama recalcularStatusEvento(eventoId)"
  origem_codigo:
    classe: "EscalaStatusDomainServiceImpl"
    metodo: "recalcularStatusPorEscalaEventoId"
  dependencias: ["REGRA_023", "REGRA_024", "REGRA_025", "REGRA_026"]

- id: REGRA_028
  nome: "consistencia_entre_entidades"
  descricao: "Membro e Igreja devem manter vínculo bidirecional valido ao criar membro."
  entidade: "Membro"
  condicao: "membro.Igreja != null && membro.idIgreja == igreja.idExterno"
  acao: "persiste vinculo Membro -> Igreja"
  origem_codigo:
    classe: "CriarMembroUseCase"
    metodo: "criarMembroSemMinisterio"
  dependencias: []

- id: REGRA_029
  nome: "Consistencia Membro e MembroMinisterio"
  descricao: "MembroMinisterio deve manter vínculo valido com Membro e Ministerio."
  entidade: "MembroMinisterio"
  condicao: "membroMinisterio.membro != null && membroMinisterio.ministerio != null"
  acao: "persiste vinculo bidirecional"
  origem_codigo:
    classe: "CriarMembroUseCase"
    metodo: "criarMembroComMinisterio"
  dependencias: []

- id: REGRA_030
  nome: "Consistencia Evento e EscalaEvento"
  descricao: "EscalaEvento deve manter vínculo valido com Evento e Ministerio."
  entidade: "EscalaEvento"
  condicao: "escalaEvento.evento != null && escalaEvento.ministerio != null"
  acao: "persiste vinculo bidirecional"
  origem_codigo:
    classe: "GerarEscalaEventoUseCase"
    metodo: "executeParaCriacao"
  dependencias: []

- id: REGRA_031
  nome: "Consistencia EscalaEvento e EscalaMinisterio"
  descricao: "EscalaMinisterio deve manter vínculo valido com EscalaEvento e MembroMinisterio."
  entidade: "EscalaMinisterio"
  condicao: "escalaMinisterio.escalaEvento != null && escalaMinisterio.membroMinisterio != null"
  acao: "persiste vinculo bidirecional"
  origem_codigo:
    classe: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase"
    metodo: "montarEscalasParaSalver"
  dependencias: []

- id: REGRA_032
  nome: "Status padrao de escala ministerio"
  descricao: "EscalaMinisterio criada tem status CONFIRMADO quando nao informado."
  entidade: "EscalaMinisterio"
  condicao: "status == null"
  acao: "define status=CONFIRMADO"
  origem_codigo:
    classe: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase"
    metodo: "montarEscalasParaSalver"
  dependencias: []

- id: REGRA_033
  nome: "Validacao de email para login"
  descricao: "Email deve existir na base para autenticacao local."
  entidade: "Membro"
  condicao: "BuscarPorEmaiUseCase.execute(email) lanca ObjectNotFoundException"
  acao: "executa dummy BCrypt e lanca BadCredentialsException"
  origem_codigo:
    classe: "LoginServiceUseCase"
    metodo: "execute"
  dependencias: []

- id: REGRA_034
  nome: "Validacao de senha para login"
  descricao: "Senha fornecida deve casar com hash BCrypt armazenado."
  entidade: "Membro"
  condicao: "!bCryptPasswordEncoder.matches(senhaFornecida, membro.senha)"
  acao: "lanca BadCredentialsException"
  origem_codigo:
    classe: "LoginServiceUseCase"
    metodo: "execute"
  dependencias: ["REGRA_033"]

- id: REGRA_035
  nome: "Validacao de audience do Google"
  descricao: "Audience do idToken deve conter clientId configurado."
  entidade: "Membro"
  condicao: "googleClientId ausente || audience == null || !audience.contains(googleClientId)"
  acao: "lanca BadCredentialsException"
  origem_codigo:
    classe: "AutenticarGoogleUseCase"
    metodo: "validarAudience"
  dependencias: []

- id: REGRA_036
  nome: "Validacao de email verificado no Google"
  descricao: "Email do Google deve estar verificado na conta."
  entidade: "Membro"
  condicao: "emailVerificado != true || email.isBlank()"
  acao: "lanca BadCredentialsException"
  origem_codigo:
    classe: "AutenticarGoogleUseCase"
    metodo: "validarEmailVerificado"
  dependencias: []

- id: REGRA_037
  nome: "Validacao de mes para consultas de periodo"
  descricao: "Mes deve estar entre 1 e 12 para consultas de periodo."
  entidade: "Evento"
  condicao: "mes < 1 || mes > 12"
  acao: "lanca FieldInvalidException"
  origem_codigo:
    classe: "ValidarMesEAno"
    metodo: "validarMesEAno"
  dependencias: []

- id: REGRA_038
  nome: "Validacao de ano para consultas de periodo"
  descricao: "Ano deve ser maior que 0 para consultas de periodo."
  entidade: "Evento"
  condicao: "ano <= 0"
  acao: "lanca FieldInvalidException"
  origem_codigo:
    classe: "ValidarMesEAno"
    metodo: "validarMesEAno"
  dependencias: []

- id: REGRA_039
  nome: "Validacao de periodo de ano para dashboard"
  descricao: "anoInicio deve ser menor ou igual a anoFim para dashboard."
  entidade: "Membro"
  condicao: "anoInicio > anoFim"
  acao: "lanca FieldInvalidException"
  origem_codigo:
    classe: "DashboardPeriodoValidator"
    metodo: "validarAnoInicioEFim"
  dependencias: []

- id: REGRA_040
  nome: "Calculo de retencao de membros"
  descricao: "Retencao eh calculada como (membrosAtivos - membrosInativos) / totalMembros."
  entidade: "Membro"
  condicao: "membrosInativos == 0 ? 100 : round((membrosAtivos - membrosInativos) * 100 / totalMembros)"
  acao: "retorna retencao no DTO de KPIs de membros"
  origem_codigo:
    classe: "BuscarKpiMembrosDashUseCase"
    metodo: "execute"
  dependencias: []

- id: REGRA_041
  nome: "Filtro de membros por ministerio na consulta"
  descricao: "Membro so aparece na consulta se pertence ao ministerio informado."
  entidade: "MembroMinisterio"
  condicao: "fkMinisterio != null && membro.ministerios.stream().anyMatch(mm -> mm.ministerio.idExterno == fkMinisterio)"
  acao: "inclui membro na lista filtrada"
  origem_codigo:
    classe: "BuscarTodosComFiltroUseCase"
    metodo: "execute"
  dependencias: []

- id: REGRA_042
  nome: "Filtro de membros por status na consulta"
  descricao: "Membro so aparece na consulta se tem o status informado."
  entidade: "Membro"
  condicao: "status != null && membro.status == status"
  acao: "inclui membro na lista filtrada"
  origem_codigo:
    classe: "BuscarTodosComFiltroUseCase"
    metodo: "execute"
  dependencias: []

- id: REGRA_043
  nome: "Validacao de lider existente em ministerio"
  descricao: "Lider informado para criar ministerio deve existir na base."
  entidade: "Membro"
  condicao: "encontreLiderPorIdExterno(idLider) == null"
  acao: "lanca ObjectNotFoundException com mensagem 'Líder do ministério não encontrado'"
  origem_codigo:
    classe: "AdicionarMinisterioUseCase"
    metodo: "execute"
  dependencias: []

- id: REGRA_044
  nome: "Promocao automatica de cargo para lider ministerio"
  descricao: "Membro promovido a lider tem seu cargo atualizado para LIDER_MINISTERIO."
  entidade: "Membro"
  condicao: "membro.cargoMembro != LIDER_MINISTERIO"
  acao: "define membro.cargoMembro = LIDER_MINISTERIO e persiste"
  origem_codigo:
    classe: "AdicionarMinisterioUseCase"
    metodo: "execute"
  dependencias: ["REGRA_043"]

- id: REGRA_045
  nome: "Status inicial ATIVO para novo ministerio"
  descricao: "Ministerio criado tem status ATIVO e data de criacao preenchida."
  entidade: "Ministerio"
  condicao: "ministerio criado"
  acao: "define status=ATIVO e dataRegistro=LocalDate.now()"
  origem_codigo:
    classe: "AdicionarMinisterioUseCase"
    metodo: "execute"
  dependencias: []

- id: REGRA_046
  nome: "Vinculacao automatica de lider ao ministerio"
  descricao: "Lider eh automaticamente vinculado como membro do ministerio ao criar."
  entidade: "MembroMinisterio"
  condicao: "ministerio criado"
  acao: "cria MembroMinisterio com cargoMembro=LIDER_MINISTERIO"
  origem_codigo:
    classe: "AdicionarMinisterioUseCase"
    metodo: "execute"
  dependencias: ["REGRA_045"]

- id: REGRA_047
  nome: "Validacao de existencia de evento para exclusao"
  descricao: "Evento so pode ser excluido se existir na base para a igreja."
  entidade: "Evento"
  condicao: "deleteByIdExterno(idExterno) retorna 0 registros afetados"
  acao: "lanca ObjectNotFoundException"
  origem_codigo:
    classe: "ApagarEventoUnicoUseCase"
    metodo: "execute"
  dependencias: []

- id: REGRA_048
  nome: "Exclusao de serie de eventos recorrentes"
  descricao: "Ao deletar multiplos eventos, o evento informado e eventos da mesma recorrencia a partir da data inicial sao removidos."
  entidade: "Evento"
  condicao: "evento encontrado dentro da igreja autenticada"
  acao: "busca por recorrencia/data inicial/igreja e executa deleteAll"
  origem_codigo:
    classe: "ApagarEventosMultiplosUseCase"
    metodo: "execute"
  dependencias: ["REGRA_047"]

- id: REGRA_049
  nome: "Validacao de vinculo membro-ministerio para remocao"
  descricao: "Membro so pode ser removido do ministerio se estiver vinculado."
  entidade: "MembroMinisterio"
  condicao: "deleteByMembroIdExternoAndMinisterioIdExterno retorna 0 registros"
  acao: "lanca ObjectNotFoundException"
  origem_codigo:
    classe: "RemoverMembroMinisterioLiderMinisterioUseCase"
    metodo: "execute"
  dependencias: []

- id: REGRA_050
  nome: "Restricao de acesso por lideranca de ministerio"
  descricao: "Apenas lider vinculado ao ministerio pode executar operacoes na escala."
  entidade: "MembroMinisterio"
  condicao: "ministerioId == null || !listaMinisteriosLider.contains(ministerioId)"
  acao: "lanca ObjectNotFoundException"
  origem_codigo:
    classe: "BuscarMembrosMinisterioPorEscalaEventoIdUseCase"
    metodo: "execute"
  dependencias: []

- id: REGRA_051
  nome: "Validacao de quantidade para randomizacao de membros"
  descricao: "Quantidade de membros a randomizar deve ser positiva e nao exceder disponibilidade."
  entidade: "EscalaMinisterio"
  condicao: "quantidadeARandomizar <= 0 || quantidadeARandomizar > membrosDisponiveis"
  acao: "lanca FieldInvalidException"
  origem_codigo:
    classe: "BuscarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase"
    metodo: "execute"
  dependencias: []

- id: REGRA_052
  nome: "Validacao de membro a trocar na revisao randomizada"
  descricao: "Membro a ser trocado deve ser informado, existir na lista selecionada e haver substituto disponivel."
  entidade: "EscalaMinisterio"
  condicao: "membroMinisterioIdASerTrocado == null || !selecionados.contains(membroMinisterioIdASerTrocado) || membrosDisponiveisParaRevisao.isEmpty()"
  acao: "lanca FieldInvalidException"
  origem_codigo:
    classe: "RevisarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase"
    metodo: "execute"
  dependencias: []

- id: REGRA_053
  nome: "Validacao de ministerio pertencente ao lider"
  descricao: "MinisterioId informado deve pertencer ao lider fazendo a consulta."
  entidade: "Ministerio"
  condicao: "ministerioId informado && !ministériosDoLider.contains(ministerioId)"
  acao: "lanca ObjectNotFoundException"
  origem_codigo:
    classe: "BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase"
    metodo: "execute"
  dependencias: ["REGRA_050"]

- id: REGRA_054
  nome: "Validacao de vinculo membro-ministerio na consulta"
  descricao: "Membro consultado deve estar vinculado ao ministerio informado."
  entidade: "MembroMinisterio"
  condicao: "ministerioId informado && !membroHasMinisterio(membroId, ministerioId)"
  acao: "lanca ObjectNotFoundException"
  origem_codigo:
    classe: "BuscarEscalaMinisterioPorMembroIdMesAnoUseCase"
    metodo: "execute"
  dependencias: []

- id: REGRA_055
  nome: "Atomicidade transacional na criacao de evento"
  descricao: "Criacao de evento, recorrencia, escalas e publicacao pos-commit sao coordenadas em transacao."
  entidade: "Evento, Recorrencia, EscalaEvento"
  condicao: "CriarEventoUseCase.execute iniciado"
  acao: "@Transactional envolve o execute garantindo rollback em erro"
  origem_codigo:
    classe: "CriarEventoUseCase"
    metodo: "execute"
  dependencias: []

- id: REGRA_056
  nome: "Restrincao de igreja para acesso de dados"
  descricao: "Qualquer operacao ler/escrever deve respeitar a igreja do JWT do usuario."
  entidade: "Igreja"
  condicao: "SecurityContext sem JWT || claim fk_igreja ausente ou invalida"
  acao: "lanca BadCredentialsException"
  origem_codigo:
    classe: "JwtUtils"
    metodo: "getIgrejaId"
  dependencias: []

- id: REGRA_057
  nome: "Consulta de ministerios do membro autenticado"
  descricao: "O membro autenticado deve possuir ministerios vinculados na mesma igreja para que a consulta retorne resultado."
  entidade: "MembroMinisterio"
  condicao: "buscaMinisteriosMembro retorna lista vazia"
  acao: "lanca ObjectNotFoundException"
  origem_codigo:
    classe: "BuscarMinisteriosMembroUseCase"
    metodo: "execute"
  dependencias: ["REGRA_056"]

- id: REGRA_058
  nome: "Validacao de configuracao OAuth Google"
  descricao: "clientId e clientSecret devem estar configurados para trocar authorization code no Google."
  entidade: "GoogleOAuth"
  condicao: "clientId ausente || clientSecret ausente"
  acao: "lanca GoogleOAuthIntegrationException"
  origem_codigo:
    classe: "GoogleAuthorizationCodeExchangerImpl"
    metodo: "validarConfiguracao"
  dependencias: []

- id: REGRA_059
  nome: "Validacao da resposta de token Google"
  descricao: "Resposta da troca de authorization code deve conter id_token."
  entidade: "GoogleTokenResponse"
  condicao: "tokenResponse == null || idToken == null || idToken.isBlank()"
  acao: "lanca GoogleOAuthIntegrationException"
  origem_codigo:
    classe: "GoogleAuthorizationCodeExchangerImpl"
    metodo: "exchange"
  dependencias: ["REGRA_058"]

- id: REGRA_060
  nome: "Traducao de erro do authorization code Google"
  descricao: "Erros de resposta do Google na troca do authorization code sao traduzidos para excecoes especificas."
  entidade: "GoogleOAuth"
  condicao: "redirect_uri_mismatch || invalid_grant || invalid_request || resposta 4xx"
  acao: "lanca GoogleAuthorizationCodeException"
  origem_codigo:
    classe: "GoogleAuthorizationCodeExchangerImpl"
    metodo: "traduzirErroRespostaGoogle"
  dependencias: ["REGRA_058"]

- id: REGRA_061
  nome: "Restricao de igreja para refresh token Google"
  descricao: "Refresh token Google existente so pode ser atualizado pela mesma igreja do registro."
  entidade: "GoogleRefreshTokenMembro"
  condicao: "tokenExistente.igrejaId != igrejaId"
  acao: "lanca BadCredentialsException"
  origem_codigo:
    classe: "AtualizarSecretGoogleUseCase"
    metodo: "validarEscopoIgreja"
  dependencias: ["REGRA_056"]

- id: REGRA_062
  nome: "Persistencia opcional de refresh token Google"
  descricao: "Refresh token retornado pelo Google so e persistido quando nao esta vazio."
  entidade: "GoogleRefreshTokenMembro"
  condicao: "googleTokenResponse.refreshToken() != null && !googleTokenResponse.refreshToken().isBlank()"
  acao: "executa AtualizarSecretGoogleUseCase.execute"
  origem_codigo:
    classe: "LoginGoogleUseCase"
    metodo: "execute"
  dependencias: ["REGRA_061"]

- id: REGRA_063
  nome: "Validacao de payload do login local"
  descricao: "Payload de login local deve conter email e senha preenchidos."
  entidade: "Membro"
  condicao: "loginRequestDTO == null || email em branco || senha em branco"
  acao: "lanca BadCredentialsException"
  origem_codigo:
    classe: "LoginServiceUseCase"
    metodo: "validateLoginRequest"
  dependencias: []

- id: REGRA_064
  nome: "Validacao de ids obrigatorios para salvar escala ministerio"
  descricao: "Salvar escala ministerial exige escalaEventoId, igrejaId e membroId."
  entidade: "EscalaMinisterio"
  condicao: "escalaEventoId == null || igrejaId == null || membroId == null"
  acao: "lanca FieldInvalidException"
  origem_codigo:
    classe: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase"
    metodo: "validarIdsObrigatorios"
  dependencias: []

- id: REGRA_065
  nome: "Validacao de item nulo na escala ministerio"
  descricao: "Lista de escala ministerial nao pode conter itens nulos."
  entidade: "EscalaMinisterio"
  condicao: "escalasMinisterio.stream().anyMatch(item == null)"
  acao: "lanca FieldInvalidException"
  origem_codigo:
    classe: "SalvarEscalaMinisterioPorEscalaEventoIdUseCase"
    metodo: "validarRequest"
  dependencias: ["REGRA_017"]

- id: REGRA_066
  nome: "Validacao de ministerio escalado na escala evento"
  descricao: "Item marcado como ministerio escalado deve informar idExternoMinisterio."
  entidade: "EscalaEvento"
  condicao: "isMinisterioEscalado == true && idExternoMinisterio == null"
  acao: "lanca FieldInvalidException"
  origem_codigo:
    classe: "AtualizarEscalaEventoPorEventoIdUseCase"
    metodo: "validarListaEscalaEvento"
  dependencias: ["REGRA_016"]

- id: REGRA_067
  nome: "Restricao de igreja para lider de ministerio"
  descricao: "Lider informado na criacao de ministerio deve pertencer a igreja do token."
  entidade: "Membro"
  condicao: "liderMinisterio.igreja == null || liderMinisterio.igreja.idExterno != igrejaIdToken"
  acao: "lanca ObjectNotFoundException"
  origem_codigo:
    classe: "AdicionarMinisterioUseCase"
    metodo: "execute"
  dependencias: ["REGRA_043", "REGRA_056"]

- id: REGRA_068
  nome: "Atualizacao de lider do ministerio"
  descricao: "Novo lider precisa existir, pertencer a mesma igreja e assumir cargo LIDER_MINISTERIO."
  entidade: "MembroMinisterio"
  condicao: "idLiderNovo informado"
  acao: "atualiza vinculo de lider e rebaixa lider antigo quando nao lidera outro ministerio"
  origem_codigo:
    classe: "EditarMinisterioUseCase"
    metodo: "atualizarLider"
  dependencias: ["REGRA_056"]

- id: REGRA_069
  nome: "Restricao de igreja para exclusao de evento"
  descricao: "Evento so pode ser excluido quando pertence a igreja autenticada."
  entidade: "Evento"
  condicao: "evento.igreja == null || evento.igreja.idExterno != igrejaId"
  acao: "lanca ObjectNotFoundException"
  origem_codigo:
    classe: "ApagarEventoUnicoUseCase"
    metodo: "execute"
  dependencias: ["REGRA_056"]

ANALISE

duplicadas: []

inconsistentes: []

ausentes: []
