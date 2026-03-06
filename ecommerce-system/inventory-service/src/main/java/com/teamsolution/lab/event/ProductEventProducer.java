package com.teamsolution.lab.event;

import com.teamsolution.lab.kafka.enums.ProductEventType;
import com.teamsolution.lab.kafka.messaging.KafkaTopics;
import com.teamsolution.lab.kafka.messaging.ProductEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventProducer {
    private final KafkaTemplate<String, ProductEventMessage> kafkaTemplate;

    public void publishProductCreated(UUID productId) {
        publish(productId, ProductEventType.CREATED);
    }

    public void publishProductUpdated(UUID productId) {
        publish(productId, ProductEventType.UPDATED);
    }

    public void publishProductDeleted(UUID productId) {
        publish(productId, ProductEventType.DELETED);
    }

    private void publish(UUID productId, ProductEventType eventType) {
        ProductEventMessage message = ProductEventMessage.builder()
                .eventType(eventType)
                .productId(productId.toString())
                .build();

        log.info("Publishing product event: {} for productId: {}", eventType, productId);
        kafkaTemplate.send(KafkaTopics.PRODUCT_EVENTS, productId.toString(), message);

    }
}