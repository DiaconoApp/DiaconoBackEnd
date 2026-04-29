# Diacono Backend

## 📌 Objetivo
API backend para gestão de igrejas, membros, ministérios, eventos e escalas de serviço (nível evento e nível membro), com autenticação JWT e login via Google.

## ⚙️ Funcionalidades Principais
- Cadastro interno e externo de membros com validações de duplicidade e hash de senha.
- Gestão de ministérios (visões governo/líder/membro) e composição de membros por ministério.
- Criação/atualização/exclusão de eventos, incluindo recorrência semanal e mensal.
- Geração e atualização de `EscalaEvento` a partir da lista de ministérios selecionados no evento.
- Alocação de membros em `EscalaMinisterio`, com validação de vínculo, duplicidade e conflito de horário.
- Recalculo automático de status de escala e evento via serviço de domínio.
- Dashboards de membros e ministérios (KPIs e séries históricas).
- Autenticação por email/senha e autenticação Google (id token + refresh token opcional).
- Publicação de evento criado em RabbitMQ após commit da transação.

## 🧱 Arquitetura
- `controllers` (pacote `infrastructure/controllers`): entrada HTTP e repasse para casos de uso.
- `usecases` (pacote `usecases`): orquestração de regras da aplicação por contexto (membro, ministério, evento, escalas, dashboard, auth).
- `domain` (pacote `domain`): entidades, enums, contratos de repositório e serviço de domínio (`EscalaStatusDomainService`).
- `infrastructure` (pacote `infrastructure`): persistência Spring Data/JPA, autenticação externa Google, mensageria e integrações técnicas.

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
  - Persiste `Evento` e gera `EscalaEvento` com `GerarEscalaEventoUseCase`.
  - Publica mensagem de evento criado (`EventoProducer`) após commit.
- Atualização de escala de evento (governo):
  - `EscalaEventoController` -> `AtualizarEscalaEventoPorEventoIdUseCase`.
  - Recebe lista de ministérios escalados, recalcula `EscalaEvento` do evento e salva.
  - Chama `EscalaStatusDomainService.recalcularStatusEvento(eventoId)`.
- Salvar escala de ministério (líder):
  - `EscalaMinisterioController` -> `SalvarEscalaMinisterioPorEscalaEventoIdUseCase`.
  - Valida request, vínculo do líder com o ministério, pertencimento dos membros e conflitos de horário.
  - Substitui escala de membros da `EscalaEvento`.
  - Chama `EscalaStatusDomainService.recalcularStatusPorEscalaEventoId(escalaEventoId)`.
  - O serviço recalcula status da `EscalaEvento` e propaga para o `Evento`.

## 🧠 Regras de Negócio Críticas
- Cadastro de membro:
  - Não permite `email`/`cpf` duplicados.
  - Sempre aplica BCrypt na senha antes de persistir.
  - Cadastro externo força cargo `MEMBRO` e status `ATIVO`.
  - Se cargo for `LIDER_MINISTERIO`, deve haver ministério associado.
- Evento:
  - Em recorrência diferente de `NAO_REPETE`, exige início/fim de recorrência válidos.
  - Recorrência não pode ultrapassar 365 dias.
  - `dataInicioRecorrencia` deve ser igual à data de início do evento.
  - Horário fim deve ser maior que início.
  - Evento no passado é bloqueado (com margem de 1 minuto).
  - Endereço completo é obrigatório quando não há `idExterno` de endereço.
- Escalas:
  - Atualização de `EscalaEvento` exige lista não vazia.
  - Em `EscalaMinisterio`, ids de membro são obrigatórios e sem duplicidade.
  - Membro escalado precisa pertencer ao ministério da escala.
  - Membro com conflito de horário é bloqueado.
  - `EscalaMinisterio` sem status informado assume `CONFIRMADO`.
- Propagação de status:
  - `EscalaEvento` fica `CONFIRMADO` apenas quando todos os `EscalaMinisterio` da escala estão confirmados; caso contrário, `PENDENTE`.
  - `Evento` fica `CONFIRMADO` apenas quando todos os `EscalaEvento` do evento estão confirmados; caso contrário, `PENDENTE`.

## 🌐 API
- Autenticação:
  - `POST /api/v1/auth/login`
  - `POST /api/v1/auth/google`
- Cadastro externo:
  - `GET /register`
  - `POST /register`
- Membros:
  - `POST /api/v1/membros`
  - `GET /api/v1/membros`
  - `GET /api/v1/membros/{idExterno}`
  - `PATCH /api/v1/membros/{idExterno}`
  - `GET /perfil`
  - `PATCH /perfil`
- Ministérios:
  - `GET /api/v1/ministerios`
  - `GET /api/v1/ministerios/governo`
  - `POST /api/v1/ministerios/governo`
  - `PATCH /api/v1/ministerios/governo/{idMinisterio}`
  - `GET /api/v1/ministerios/lider-ministerio`
  - `GET /api/v1/ministerios/membro`
- Eventos:
  - `GET /api/v1/eventos`
  - `GET /api/v1/eventos/{id}`
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
  - `PATCH /api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}`
  - `GET /api/v1/escalas-ministerio/membro`
- Dashboards:
  - `GET /api/v1/dashboards/membros/kpis`
  - `GET /api/v1/dashboards/ministerios/kpis`