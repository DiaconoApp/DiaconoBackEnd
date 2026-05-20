# Fluxo Negativo de Membros (Bruno)

Colecao para validar falha esperada de regra de negocio no cadastro interno de membro.

## O que este fluxo cobre

- tentativa de criar `LIDER_MINISTERIO` sem `idExternoMinisterios`

## Credenciais utilizadas

- `emailLiderMinisterio` e `senhaLiderMinisterio`

## Sequencia

1. Login lider
2. Tentar criar lider sem ministerio
