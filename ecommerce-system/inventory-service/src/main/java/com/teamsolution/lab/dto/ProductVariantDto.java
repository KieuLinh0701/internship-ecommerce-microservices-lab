package com.teamsolution.lab.dto;

import com.teamsolution.lab.enums.ProductVariantStatus;
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
  private String color;
  private Long price;
  private String imageUrl;
  private ProductVariantStatus status;
  private ProductVariantInventoryDto inventory;
}
