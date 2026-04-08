package com.teamsolution.search.kafka.consumer;

import com.teamsolution.common.core.exception.TemporaryException;
import com.teamsolution.common.core.exception.enums.CommonErrorCode;
import com.teamsolution.common.kafka.event.inventory.ProductStatusChangedEvent;
import com.teamsolution.common.kafka.topics.KafkaTopics;
import com.teamsolution.search.service.ProcessedEventService;
import com.teamsolution.search.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductStatusChangeEventConsumer {
  private final ProcessedEventService processedEventService;
  private final ProductSearchService productSearchService;

  @KafkaListener(
      topics = KafkaTopics.PRODUCT_STATUS_CHANGED,
      groupId = "${spring.kafka.consumer.group-id}")
  @Transactional
  public void handle(ProductStatusChangedEvent event) {
    log.info(
        "[Notification][ProductStatusChangedEvent] Received event id={}, productId={}",
        event.getId(),
        event.getProductId());

    if (processedEventService.isDuplicate(event.getId())) {
      log.info(
          "[Notification][ProductStatusChangedEvent] Duplicate event, skipping id={}",
          event.getId());
      return;
    }

    log.info(
        "[Notification][ProductStatusChangedEvent] Processing notification for productId={}",
        event.getProductId());

    try {
      productSearchService.updateStatus(event);
    } catch (Exception ex) {
      throw new TemporaryException(CommonErrorCode.DATABASE_ERROR, ex);
    }

    processedEventService.markProcessed(event.getId());

    log.info(
        "[Notification][ProductStatusChangedEvent] Completed notification for productId={}",
        event.getProductId());
  }

  // Test Retry by throwing TemporaryException
  //    @KafkaListener(
  //            topics = KafkaTopics.PRODUCT_STATUS_CHANGED,
  //            groupId = "${spring.kafka.consumer.group-id}")
  //    public void handle(
  //            ProductStatusChangedEvent event,
  //            @Header(name = KafkaHeaders.DELIVERY_ATTEMPT, required = false) Integer attempt
  //    ) {
  //        log.info("Attempt: {}", attempt);
  //
  //        throw new TemporaryException(ErrorCode.PRODUCT_NOT_FOUND);
  //    }
}
