package com.teamsolution.lab.dto;

import com.teamsolution.lab.enums.ProductVariantStatus;
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
public class ProductVariantDto {
  private UUID id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private UUID productId;
  private String sku;
  private UUID sizeId;
  private String color;
  private Long price;
  private String imageUrl;
  private ProductVariantStatus status;
  private ProductVariantInventoryDto inventory;
  private UUID createdBy;
  private UUID updatedBy;
  private Boolean isDelete;
  private Long version;
}
