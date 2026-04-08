package com.teamsolution.notification.service;

import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface FailedEventSaverService {

  void saveFailedEvent(ConsumerRecord<?, ?> record, Exception ex);

  void markSuccess(UUID id);

  boolean existsByEventId(UUID id);
}
