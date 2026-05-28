# Diacono Backend

## 📌 Objetivo
API backend para gestão de igrejas, membros, ministérios, eventos e escalas de serviço (nível evento e nível membro), com autenticação JWT, login local por email/senha e login Google por authorization code.

## ⚙️ Funcionalidades Principais
- Cadastro interno e externo de membros com validações de duplicidade e hash de senha.
- Gestão de ministérios nas visões governo, líder e membro, incluindo criação, edição, troca de líder e composição de membros.
- Criação/atualização/exclusão de eventos, incluindo recorrência semanal e mensal.
- Geração e atualização de `EscalaEvento` a partir dos ministérios escalados no evento.
- Alocação, consulta, randomização e revisão de membros em `EscalaMinisterio`, com validação de vínculo, duplicidade e conflito de horário.
- Recálculo automático de status de escala e evento via serviço de domínio.
- Dashboards de membros e ministérios com KPIs, séries históricas e distribuições.
- Autenticação por email/senha e autenticação Google com troca de authorization code, validação de id token e refresh token opcional.
- Publicação de evento criado em RabbitMQ após commit da transação.

## 🧱 Arquitetura
- `controllers` (pacote `infrastructure/controllers`): entrada HTTP e repasse para casos de uso.
- `applications` (pacote `applications`): DTOs e mapeadores usados na comunicação da API.
- `usecases` (pacote `usecases`): orquestração de regras da aplicação por contexto (membro, ministério, evento, escalas, dashboard, auth).
- `domain` (pacote `domain`): entidades, enums, contratos de repositório e serviço de domínio (`EscalaStatusDomainService`).
- `infrastructure` (pacote `infrastructure`): persistência Spring Data/JPA, autenticação Google, mensageria e integrações técnicas.
- `global` (pacote `global`): segurança, configuração, tratamento de erros, utilitários JWT e criptografia de campos sensíveis.

## 🛠️ Tecnologias
- Linguagem:
  - Java 21
- Frameworks e libs principais:
  - Spring Boot 3.5.5
  - Spring Web
  - Spring Data JPA / JDBC
  - Spring Validation
  - Spring Security + OAuth2 Resource Server + OAuth2 Client
  - Spring AMQP + Spring Retry
  - SpringDoc OpenAPI (Swagger UI)
  - MapStruct
  - Lombok
  - Maven
  - Banco em produção: MariaDB
  - Banco em desenvolvimento: H2 em memória (`application.properties`)

## 🧠 Domínio
- Entidades principais:
  - `Igreja`: contexto organizacional do tenant.
  - `Membro`: usuário da igreja com cargo (`MEMBRO`, `LIDER_MINISTERIO`, `GOVERNO`) e status.
  - `Ministerio`: equipe da igreja com líder e status.
  - `MembroMinisterio`: vínculo entre membro e ministério (N:N) com cargo no ministério.
  - `Evento`: agenda principal com organizador, endereço e recorrência.
  - `Recorrencia`: regra temporal do evento (`NAO_REPETE`, `SEMANAL`, `MENSAL`).
  - `EscalaEvento`: ministérios escalados para um evento (status da escala do ministério no evento).
  - `EscalaMinisterio`: membros escalados para uma `EscalaEvento`.
  - `GoogleRefreshTokenMembro`: armazenamento do refresh token Google por membro.
  - `EnderecoMembro`, `EnderecoEvento`, `EnderecoIgreja`: endereços vinculados às entidades principais.
- Relações centrais:
  - `Igreja` 1:N `Membro` e 1:N `Ministerio`.
  - `Membro` N:N `Ministerio` via `MembroMinisterio`.
  - `Evento` pertence a uma `Igreja`, tem `organizador` (`Membro`) e pode ter `Recorrencia`.
  - `Evento` 1:N `EscalaEvento`.
  - `EscalaEvento` 1:N `EscalaMinisterio`.

## 🔄 Fluxos Críticos
- Criação de evento:
  - `EventoController` -> `CriarEventoUseCase`.
  - Valida recorrência, endereço e horário.
  - Persiste evento único ou série recorrente semanal/mensal.
  - Gera `EscalaEvento` com `GerarEscalaEventoUseCase`.
  - Publica mensagem de evento criado (`EventoProducer`) após commit.
- Atualização de escala de evento (governo):
  - `EscalaEventoController` -> `AtualizarEscalaEventoPorEventoIdUseCase`.
  - Recebe lista de ministérios escalados, substitui/atualiza `EscalaEvento` e remove escalas ministeriais incompatíveis.
  - Chama `EscalaStatusDomainService.recalcularStatusEvento(eventoId)`.
- Salvar escala de ministério (líder):
  - `EscalaMinisterioController` -> `SalvarEscalaMinisterioPorEscalaEventoIdUseCase`.
  - Valida request, vínculo do líder com o ministério, pertencimento dos membros e conflitos de horário.
  - Substitui escala de membros da `EscalaEvento`.
  - Chama `EscalaStatusDomainService.recalcularStatusPorEscalaEventoId(escalaEventoId)`.
  - O serviço recalcula status da `EscalaEvento` e propaga para o `Evento`.
- Login Google:
  - `GoogleAuthController` -> `LoginGoogleUseCase`.
  - Troca authorization code por tokens no Google.
  - Valida id token, audience, email verificado e usuário local.
  - Atualiza refresh token quando retornado pelo Google e emite JWT da API.

## 🧠 Regras de Negócio Críticas
- Cadastro de membro:
  - Não permite `email`/`cpf` duplicados.
  - Sempre aplica BCrypt na senha antes de persistir.
  - Cadastro externo força cargo `MEMBRO` e status `ATIVO`.
  - Cadastro interno usa o cargo informado no DTO.
  - Se cargo for `LIDER_MINISTERIO`, deve haver ministério associado.
- Evento:
  - Em recorrência diferente de `NAO_REPETE`, exige início/fim de recorrência válidos.
  - Recorrência não pode ultrapassar 365 dias.
  - `dataInicioRecorrencia` deve ser igual à data de início do evento.
  - Horário fim não pode ser anterior ao horário de início.
  - Evento no passado é bloqueado (com margem de 1 minuto).
  - Endereço completo é obrigatório quando não há `idExterno` de endereço.
  - Exclusão de evento exige que o evento pertença à igreja autenticada.
  - Exclusão múltipla remove o evento informado e eventos futuros da mesma recorrência.
- Escalas:
  - Atualização de `EscalaEvento` exige lista não vazia.
  - Item marcado como ministério escalado deve informar `idExternoMinisterio`.
  - Em `EscalaMinisterio`, ids obrigatórios devem estar preenchidos e sem duplicidade.
  - Lista de `EscalaMinisterio` não pode ser nula nem conter itens nulos.
  - Membro escalado precisa pertencer ao ministério da escala.
  - Membro com conflito de horário é bloqueado.
  - `EscalaMinisterio` sem status informado assume `CONFIRMADO`.
- Propagação de status:
  - `EscalaEvento` fica `CONFIRMADO` apenas quando todos os `EscalaMinisterio` da escala estão confirmados; caso contrário, `PENDENTE`.
  - `Evento` fica `CONFIRMADO` apenas quando todos os `EscalaEvento` do evento estão confirmados; caso contrário, `PENDENTE`.
- Autenticação e escopo:
  - Operações autenticadas devem respeitar a igreja (`fk_igreja`) presente no JWT.
  - Login local exige payload com email e senha preenchidos.
  - Login Google exige configuração OAuth válida, id token retornado pelo Google, audience compatível e email verificado.
  - Refresh token Google só é atualizado quando pertence à mesma igreja do membro.
- Ministério:
  - Líder informado na criação deve existir e pertencer à igreja do token.
  - Novo líder de ministério assume cargo `LIDER_MINISTERIO`.
  - Líder antigo volta para `MEMBRO` apenas quando não lidera outro ministério.

## 🌐 API
- Autenticação:
  - `POST /api/v1/auth/login`
  - `POST /api/v1/auth/google`
- Cadastro externo:
  - `GET /api/v1/register`
  - `POST /api/v1/register`
- Membros:
  - `POST /api/v1/membros`
  - `GET /api/v1/membros`
  - `GET /api/v1/membros/{idExterno}`
  - `PATCH /api/v1/membros/{idExterno}`
  - `GET /api/v1/perfil`
  - `PATCH /api/v1/perfil`
- Ministérios:
  - `GET /api/v1/ministerios`
  - `GET /api/v1/ministerios/governo`
  - `POST /api/v1/ministerios/governo`
  - `PATCH /api/v1/ministerios/governo/{idMinisterio}`
  - `GET /api/v1/ministerios/lider-ministerio`
  - `GET /api/v1/ministerios/lider-ministerio/{idMinisterio}`
  - `PATCH /api/v1/ministerios/lider-ministerio/{idMinisterio}`
  - `DELETE /api/v1/ministerios/lider-ministerio/{idMinisterio}/{idMembroMinisterio}`
  - `GET /api/v1/ministerios/membro`
- Eventos:
  - `GET /api/v1/eventos`
  - `GET /api/v1/eventos/{id}`
  - `GET /api/v1/eventos/enderecos`
  - `POST /api/v1/eventos`
  - `PATCH /api/v1/eventos/{id}`
  - `DELETE /api/v1/eventos/unico/{id}`
  - `DELETE /api/v1/eventos/multiplos/{id}`
- Escalas de evento:
  - `GET /api/v1/escalas-evento/governo`
  - `GET /api/v1/escalas-evento/governo/{eventoId}`
  - `PATCH /api/v1/escalas-evento/governo/{eventoId}`
- Escalas de ministério:
  - `GET /api/v1/escalas-ministerio/lider-ministerio`
  - `GET /api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}/membros-disponiveis`
  - `GET /api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}`
  - `GET /api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}/{quantidadeMembrosRandomizados}`
  - `POST /api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}/revisar-randomizacao/{membroMinisterioIdASerTrocado}`
  - `PATCH /api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}`
  - `GET /api/v1/escalas-ministerio/membro`
- Dashboards:
  - `GET /api/v1/dashboards/membros/kpis`
  - `GET /api/v1/dashboards/membros/evolucao`
  - `GET /api/v1/dashboards/membros/faixa-etaria`
  - `GET /api/v1/dashboards/membros/genero`
  - `GET /api/v1/dashboards/ministerios/kpis`
  - `GET /api/v1/dashboards/ministerios/evolucao/{idMinisterio}`
  - `GET /api/v1/dashboards/ministerios/quantidade-membros`
  - `GET /api/v1/dashboards/ministerios/quantidade-eventos`
