package com.diacono.diacono.infrastructure.messaging;

import com.diacono.diacono.applications.dtos.evento.EventoCriadoMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EventoConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventoConsumer.class);

    @RabbitListener(queues = "${app.rabbit.queue.evento-criado}")
    public void consumirEventoCriado(EventoCriadoMessageDTO message) {
        LOGGER.info("EventoCriado recebido idExternoEvento={} nome={} dataHoraInicio={}",
                message.idExternoEvento(),
                message.nome(),
                message.dataHoraInicio());
    }
}

