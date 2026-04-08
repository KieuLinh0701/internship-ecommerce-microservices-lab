package com.teamsolution.notification.kafka.consumer;

import com.teamsolution.common.kafka.event.notification.AuthNotificationEvent;
import com.teamsolution.common.kafka.topics.KafkaTopics;
import com.teamsolution.notification.service.NotificationDispatcher;
import com.teamsolution.notification.service.ProcessedEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthNotificationEventConsumer {
  private final NotificationDispatcher notificationDispatcher;
  private final ProcessedEventService processedEventService;

  @KafkaListener(
      topics = KafkaTopics.AUTH_NOTIFICATION,
      groupId = "${spring.kafka.consumer.group-id}")
  @Transactional
  public void handle(AuthNotificationEvent event) {
    log.info(
        "[Notification][AuthNotificationEvent] Received event id={}, accountId={}",
        event.getId(),
        event.getAccountId());

    if (processedEventService.isDuplicate(event.getId())) {
      log.info(
          "[Notification][AuthNotificationEvent] Duplicate event, skipping id={}", event.getId());
      return;
    }

    log.info(
        "[Notification][AuthNotificationEvent] Processing notification for accountId={}",
        event.getAccountId());

    notificationDispatcher.send(
        event.getAccountId(),
        event.getAccountRoleId(),
        event.getEmail(),
        event.getType(),
        event.getVariables(),
        event.getChannels());

    processedEventService.markProcessed(event.getId());

    log.info(
        "[Notification][AuthNotificationEvent] Completed notification for accountId={}",
        event.getAccountId());
  }

  // Test Retry by throwing TemporaryException
  //    @KafkaListener(
  //            topics = KafkaTopics.AUTH_NOTIFICATION,
  //            groupId = "${spring.kafka.consumer.group-id}")
  //    public void handle(
  //            AuthNotificationEvent event,
  //            @Header(name = KafkaHeaders.DELIVERY_ATTEMPT, required = false) Integer attempt
  //    ) {
  //        log.info("Attempt: {}", attempt);
  //
  //        throw new TemporaryException(ErrorCode.NOTIFICATION_NOT_FOUND);
  //    }
}
