# Fluxo de Confirmacao de Escalas (Bruno)

Colecao para validar propagacao de status no fluxo:

- `EscalaMinisterio -> EscalaEvento -> Evento`

## O que esta colecao usa

- Endpoints reais dos controllers:
  - `POST /api/v1/auth/login`
  - `POST /api/v1/eventos`
  - `GET /api/v1/eventos?mes&ano`
  - `GET /api/v1/eventos/{id}`
  - `GET /api/v1/escalas-evento/governo/{eventoId}`
  - `GET /api/v1/escalas-evento/governo?mes&ano&nomeEvento`
  - `GET /api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}`
  - `PATCH /api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}`

## Variaveis de ambiente

Arquivo: `environments/local.bru`

- `emailGoverno` e `senhaGoverno`: credenciais de governo para criar evento e consultar `escalas-evento/governo`
- `emailLiderMinisterio` e `senhaLiderMinisterio`: credenciais de lider para consultar e salvar `escalas-ministerio/lider-ministerio`
- `ministerioId1` e `ministerioId2`: dois ministerios existentes no `data.sql`
- `membroMinisterioId1` e `membroMinisterioId2`: membros validos dos ministerios escolhidos
- `dataHoraInicio` e `dataHoraFim`: horario do evento a ser criado; `mesEvento` e `anoEvento` sao sincronizados automaticamente a partir de `dataHoraInicio`

## Sequencia do fluxo

1. Login
2. Login lider
3. Criar evento com 2 ministerios
4. Capturar `eventoId`
5. Capturar `escalaEventoId1` e `escalaEventoId2`
6. Validacoes iniciais
7. Alimentar primeira escala
8. Validacoes intermediarias
9. Alimentar segunda escala
10. Validacoes finais

## Observacoes sobre contrato da API

- O fluxo usa credencial de `GOVERNO` porque os endpoints `escalas-evento/governo` exigem esse escopo.
- O fluxo troca para credencial de `LIDER_MINISTERIO` nas etapas de `escalas-ministerio`, porque os use cases validam vínculo real do lider com o ministerio da escala.
- O endpoint `GET /api/v1/eventos/{id}` expoe `status` no DTO atual e pode ser usado como validacao complementar do estado do evento.
