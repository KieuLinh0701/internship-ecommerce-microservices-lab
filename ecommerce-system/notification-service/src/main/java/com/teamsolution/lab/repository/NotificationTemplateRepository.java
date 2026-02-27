package com.teamsolution.lab.repository;

import com.teamsolution.lab.entity.NotificationTemplate;
import java.util.Optional;
import java.util.UUID;

public interface NotificationTemplateRepository extends BaseRepository<NotificationTemplate, UUID> {
  Optional<NotificationTemplate> findByEventType(String eventType);
}
