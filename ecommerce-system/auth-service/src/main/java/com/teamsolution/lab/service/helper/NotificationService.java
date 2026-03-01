package com.teamsolution.lab.service.helper;

import com.teamsolution.lab.config.properties.RabbitMQProperties;
import com.teamsolution.lab.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final AmqpTemplate amqpTemplate;
    private final RabbitMQProperties rabbitMQProperties;

    public void sendVerificationEmail(String email, String rawOtp) {
        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .email(email)
                .rawOtp(rawOtp)
                .build();
        amqpTemplate.convertAndSend(
                rabbitMQProperties.getExchange(),
                rabbitMQProperties.getRoutingKey(), event
        );
    }
}
