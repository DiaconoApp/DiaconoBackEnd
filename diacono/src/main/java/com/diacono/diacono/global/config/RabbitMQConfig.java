package com.diacono.diacono.global.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange appExchange(@Value("${app.rabbit.exchange}") String exchangeName) {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public Queue eventoCriadoQueue(@Value("${app.rabbit.queue.evento-criado}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding eventoCriadoBinding(
            Queue eventoCriadoQueue,
            TopicExchange appExchange,
            @Value("${app.rabbit.routing-key.evento-criado}") String routingKey) {
        return BindingBuilder.bind(eventoCriadoQueue).to(appExchange).with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

