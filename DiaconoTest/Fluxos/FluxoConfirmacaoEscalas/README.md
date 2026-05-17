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

- `email` e `senha`: credenciais para login
- `ministerioId1` e `ministerioId2`: dois ministerios existentes no `data.sql`
- `membroMinisterioId1` e `membroMinisterioId2`: membros validos dos ministerios escolhidos

## Sequencia do fluxo

1. Login
2. Criar evento com 2 ministerios
3. Capturar `eventoId`
4. Capturar `escalaEventoId1` e `escalaEventoId2`
5. Validacoes iniciais
6. Alimentar primeira escala
7. Validacoes intermediarias
8. Alimentar segunda escala
9. Validacoes finais

## Observacao sobre contrato da API

O endpoint `GET /api/v1/eventos/{id}` nao expoe campo `status` no DTO atual.
Por isso, a verificacao de status do evento foi feita em:

- `GET /api/v1/escalas-evento/governo?mes&ano&nomeEvento`

Esse endpoint retorna `EscalaEventoConsolidadoDTO.status` (status do evento).

