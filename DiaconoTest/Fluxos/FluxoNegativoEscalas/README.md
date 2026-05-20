# Fluxo Negativo de Escalas (Bruno)

Colecao para validar falhas esperadas de regra de negocio em escalas.

## O que este fluxo cobre

- membro duplicado na escala
- membro fora do ministerio da escala
- membro com conflito de horario

## Credenciais utilizadas

- `emailGoverno` e `senhaGoverno`
- `emailLiderMinisterio` e `senhaLiderMinisterio`

## Variaveis de ambiente utilizadas

- `ministerioId1`
- `ministerioId2`
- `membroMinisterioId1`
- `membroMinisterioId2`

## Sequencia

1. Login governo
2. Login lider
3. Criar evento base com dois ministerios
4. Capturar ids das escalas do evento base
5. Salvar uma escala valida para preparar conflito
6. Validar membro duplicado
7. Validar membro fora do ministerio
8. Criar segundo evento no mesmo horario
9. Capturar escala do segundo evento
10. Validar conflito de horario
