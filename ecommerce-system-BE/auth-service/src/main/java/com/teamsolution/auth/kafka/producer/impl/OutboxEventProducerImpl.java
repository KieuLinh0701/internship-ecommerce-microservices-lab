package com.teamsolution.auth.kafka.producer.impl;

import com.teamsolution.auth.entity.OutboxEvent;
import com.teamsolution.auth.enums.AuthEventType;
import com.teamsolution.auth.kafka.producer.OutboxEventProducer;
import com.teamsolution.auth.service.OutboxEventService;
import com.teamsolution.common.core.exception.AppException;
import com.teamsolution.common.core.exception.enums.CommonErrorCode;
import com.teamsolution.common.core.util.JsonUtils;
import com.teamsolution.common.kafka.event.notification.AuthNotificationEvent;
import com.teamsolution.common.kafka.topics.KafkaTopics;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxEventProducerImpl implements OutboxEventProducer {

  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final OutboxEventService outboxEventService;

  @Override
  @Transactional
  public void publishEvent(OutboxEvent event) {
    String topic = getTopicForEvent(AuthEventType.valueOf(event.getEventType()));
    Object payload = parsePayload(AuthEventType.valueOf(event.getEventType()), event.getPayload());
    Message<Object> message =
        org.springframework.messaging.support.MessageBuilder.withPayload(payload)
            .setHeader(KafkaHeaders.TOPIC, topic)
            .setHeader(KafkaHeaders.KEY, String.valueOf(event.getAggregateId()))
            .setHeader("eventType", event.getEventType())
            .setHeader("eventId", event.getId())
            .build();

    CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(message);

    future.whenComplete(
        (result, ex) -> {
          if (ex != null) {
            outboxEventService.handleFailure(event.getId(), ex);
          } else {
            outboxEventService.markSent(event.getId());
          }
        });
  }

  private Object parsePayload(AuthEventType eventType, String payload) {
    return switch (eventType) {
      case OTP_SENT_FOR_PENDING_LOGIN -> JsonUtils.fromJson(payload, AuthNotificationEvent.class);
      case OTP_SENT_FOR_CHANGE_EMAIL -> JsonUtils.fromJson(payload, AuthNotificationEvent.class);
      default -> throw new AppException(CommonErrorCode.UNKNOWN_EVENT_TYPE);
    };
  }

  private String getTopicForEvent(AuthEventType eventType) {
    return switch (eventType) {
      case AuthEventType.OTP_SENT_FOR_PENDING_LOGIN -> KafkaTopics.AUTH_NOTIFICATION;
      case AuthEventType.OTP_SENT_FOR_CHANGE_EMAIL -> KafkaTopics.AUTH_NOTIFICATION;
      default -> throw new AppException(CommonErrorCode.UNKNOWN_EVENT_TYPE);
    };
  }
}
