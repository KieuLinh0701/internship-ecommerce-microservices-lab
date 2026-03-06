package com.teamsolution.lab.messaging;

import com.teamsolution.lab.enums.ProductEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEventMessage {
  private ProductEventType eventType;
  private String productId;
}
