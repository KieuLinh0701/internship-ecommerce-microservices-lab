package com.teamsolution.lab.consumer;

import com.rabbitmq.client.Channel;
import com.teamsolution.lab.event.UserRegisteredEvent;
import com.teamsolution.lab.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void consume(UserRegisteredEvent event,
                        Channel channel,
                        @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.info("Received message: {}", event.getEmail());
        try {

            emailService.sendWelcomeEmail(event);

            channel.basicAck(tag, false);

        } catch (Exception e) {
            log.error("Failed to consume message: {}", e.getMessage(), e);
            channel.basicNack(tag, false, false);
        }
    }
}