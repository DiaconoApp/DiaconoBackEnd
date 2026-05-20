# Fluxo Negativo de Eventos (Bruno)

Colecao para validar falhas esperadas de regra de negocio em criacao de eventos.

## O que este fluxo cobre

- recorrencia sem datas
- recorrencia acima de um ano
- endereco incompleto quando `idExterno` nao eh informado

## Credenciais utilizadas

- `emailGoverno` e `senhaGoverno`

## Sequencia

1. Login governo
2. Validar recorrencia sem datas
3. Validar recorrencia acima de um ano
4. Validar endereco incompleto
