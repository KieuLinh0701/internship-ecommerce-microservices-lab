package com.teamsolution.lab.config;

import com.teamsolution.lab.config.properties.RabbitMQProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
    private final RabbitMQProperties rabbitMQProperties;

    @Bean
    public Queue emailQueue() {
        return new Queue(rabbitMQProperties.getQueue(), true);
    }

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(rabbitMQProperties.getExchange());
    }

    @Bean
    public Binding binding(Queue emailQueue, TopicExchange userExchange) {
        return BindingBuilder
                .bind(emailQueue)
                .to(userExchange)
                .with(rabbitMQProperties.getRoutingKey());
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());

        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }
}
