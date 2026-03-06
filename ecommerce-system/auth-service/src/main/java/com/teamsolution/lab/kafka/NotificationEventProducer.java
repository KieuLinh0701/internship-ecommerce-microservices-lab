package com.teamsolution.lab.kafka;

import com.teamsolution.lab.kafka.enums.NotificationEventType;
import com.teamsolution.lab.kafka.messaging.KafkaTopics;
import com.teamsolution.lab.kafka.messaging.NotificationEventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationEventProducer {
    private final KafkaTemplate<String, NotificationEventMessage> kafkaTemplate;

    // TODO: Temporary sending directly to email service via Kafka.
    //  Will be refactored to route through notification-service in the future.
    public void sendOtpEmail(String email, String rawOtp, NotificationEventType type) {
        publish(email, rawOtp, type);
    }

    private void publish(String email, String rawOtp, NotificationEventType type) {
        NotificationEventMessage event = NotificationEventMessage.builder()
                .email(email)
                .rawOtp(rawOtp)
                .type(type)
                .build();

        kafkaTemplate.send(KafkaTopics.NOTIFICATION_EVENTS, email, event);
    }
}