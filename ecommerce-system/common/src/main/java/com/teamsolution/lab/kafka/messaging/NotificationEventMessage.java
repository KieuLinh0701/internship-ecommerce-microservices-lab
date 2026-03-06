package com.teamsolution.lab.kafka.messaging;

import com.teamsolution.lab.kafka.enums.NotificationEventType;
import com.teamsolution.lab.util.UuidGenerator;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEventMessage {
  @Builder.Default private UUID eventId = UuidGenerator.generate();

  private String email;
  private String rawOtp;
  private NotificationEventType type;

  @Builder.Default private String timestamp = Instant.now().toString();
}
