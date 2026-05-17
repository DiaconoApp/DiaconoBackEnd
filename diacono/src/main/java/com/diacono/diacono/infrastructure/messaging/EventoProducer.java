package com.diacono.diacono.infrastructure.messaging;

import com.diacono.diacono.applications.dtos.evento.EventoCriadoMessageDTO;
import com.diacono.diacono.domain.entity.Evento;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class EventoProducer {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKeyEventoCriado;

    public EventoProducer(
            RabbitTemplate rabbitTemplate,
            @Value("${app.rabbit.exchange}") String exchange,
            @Value("${app.rabbit.routing-key.evento-criado}") String routingKeyEventoCriado) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKeyEventoCriado = routingKeyEventoCriado;
    }

    public void publicarEventoCriadoAposCommit(Evento evento) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    publicarEventoCriado(evento);
                }
            });
            return;
        }

        publicarEventoCriado(evento);
    }

    private void publicarEventoCriado(Evento evento) {
        rabbitTemplate.convertAndSend(exchange, routingKeyEventoCriado, toMessage(evento));
    }

    private EventoCriadoMessageDTO toMessage(Evento evento) {
        return new EventoCriadoMessageDTO(
                evento.getIdExterno(),
                evento.getNome(),
                evento.getDescricao(),
                evento.getCusto(),
                evento.getPublicoAlvo(),
                evento.getDataHoraInicio(),
                evento.getDataHoraFim(),
                evento.getEnderecoEvento().toString(),
                evento.getIgreja().getIdExterno()
        );
    }
}

