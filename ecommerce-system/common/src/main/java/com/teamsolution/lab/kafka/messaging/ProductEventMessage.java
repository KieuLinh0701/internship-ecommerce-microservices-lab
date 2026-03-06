package com.teamsolution.lab.kafka.messaging;

import com.teamsolution.lab.kafka.enums.ProductEventType;
import com.teamsolution.lab.util.UuidGenerator;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEventMessage {
  @Builder.Default private UUID eventId = UuidGenerator.generate();

  private ProductEventType eventType;
  private String productId;
}
