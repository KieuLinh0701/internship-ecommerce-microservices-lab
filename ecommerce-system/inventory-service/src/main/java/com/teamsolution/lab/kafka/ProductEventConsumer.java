package com.teamsolution.lab.kafka;

import com.teamsolution.lab.document.ProductDocument;
import com.teamsolution.lab.enums.ProductStatus;
import com.teamsolution.lab.kafka.enums.ProductEventType;
import com.teamsolution.lab.kafka.messaging.KafkaTopics;
import com.teamsolution.lab.kafka.messaging.ProductEventMessage;
import com.teamsolution.lab.mapper.ProductDocumentMapper;
import com.teamsolution.lab.repository.ProductRepository;
import com.teamsolution.lab.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductEventConsumer {
    private final ProductRepository productRepository;
    private final ProductSearchRepository productSearchRepository;
    private final ProductDocumentMapper productDocumentMapper;

    @KafkaListener(
            topics = KafkaTopics.PRODUCT_EVENTS,
            groupId = "inventory-group"
    )
    public void consume(ProductEventMessage message) {

        switch (message.getEventType()) {
            case ProductEventType.CREATED, ProductEventType.UPDATED -> syncToEs(message.getProductId());
            case ProductEventType.DELETED -> productSearchRepository.deleteById(message.getProductId());
        }
    }

    private void syncToEs(String productId) {
        productRepository.findByIdAndIsDeleteFalseAndStatus(
                        UUID.fromString(productId), ProductStatus.ACTIVE)
                .ifPresentOrElse(
                        product -> {
                            ProductDocument doc = productDocumentMapper.toDocument(product);
                            productSearchRepository.save(doc);
                        },
                        () -> {
                            productSearchRepository.deleteById(productId);
                        }
                );
    }
}
