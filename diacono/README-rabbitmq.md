# RabbitMQ no Diacono Backend

## O que foi implementado

- Exchange: `diacono.exchange`
- Queue: `diacono.evento.criado.queue`
- Routing key: `diacono.evento.criado`
- Producer: publica `EventoCriadoMessageDTO` ao salvar eventos
- Consumer: escuta a fila e registra log quando recebe `EventoCriado`

## Arquivos principais

- `src/main/java/com/diacono/diacono/global/config/RabbitMQConfig.java`
- `src/main/java/com/diacono/diacono/infrastructure/messaging/EventoProducer.java`
- `src/main/java/com/diacono/diacono/infrastructure/messaging/EventoConsumer.java`
- `src/main/java/com/diacono/diacono/applications/dtos/evento/EventoCriadoMessageDTO.java`
- `src/main/java/com/diacono/diacono/usecases/eventos/CriarEventoUseCase.java`
- `src/main/resources/application.properties`
- `docker-compose.yml`

## Como subir o RabbitMQ local

```powershell
docker compose up -d rabbitmq
```

Painel do RabbitMQ Management: `http://localhost:15672` (guest/guest)

## Como rodar o backend

```powershell
.\mvnw.cmd spring-boot:run
```

## Como validar

1. Chame o endpoint de criação de evento.
2. Verifique no log do backend a linha de consumo de evento:
   - `EventoCriado recebido idExternoEvento=...`
3. No painel do RabbitMQ, verifique a queue `diacono.evento.criado.queue`.

