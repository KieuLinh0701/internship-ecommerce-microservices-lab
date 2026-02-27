package com.teamsolution.lab.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantInventoryDto {
  private UUID id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private UUID variantId;
  private Integer quantity;
  private Integer reservedQuantity;
  private Integer availableQuantity;
  private Integer lowStockThreshold;
  private UUID createdBy;
  private UUID updatedBy;
  private Boolean isDelete;
  private Long version;
}
