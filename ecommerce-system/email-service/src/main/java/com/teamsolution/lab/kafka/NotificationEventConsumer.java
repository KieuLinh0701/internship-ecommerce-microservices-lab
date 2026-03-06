package com.teamsolution.lab.kafka;

import com.teamsolution.lab.kafka.messaging.KafkaTopics;
import com.teamsolution.lab.kafka.messaging.NotificationEventMessage;
import com.teamsolution.lab.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventConsumer {
    private final EmailService emailService;

    @KafkaListener(
            topics = KafkaTopics.NOTIFICATION_EVENTS,
            groupId = "email-group"
    )
    public void consume(NotificationEventMessage message) {
        log.info("Received notification event: {} for email: {}",
                message.getType(), message.getEmail());

        switch (message.getType()) {
            case EMAIL_VERIFICATION -> emailService.sendVerificationEmail(
                    message.getEmail(),
                    message.getRawOtp()
            );
            case PASSWORD_RESET -> emailService.sendPasswordResetEmail(
                    message.getEmail(),
                    message.getRawOtp()
            );
            default -> log.warn("Unknown notification type: {}", message.getType());
        }
    }
}
