package com.teamsolution.lab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantInventoryDto {
  private UUID id;
  private UUID variantId;
  private Integer quantity;
  private Integer reservedQuantity;
}
