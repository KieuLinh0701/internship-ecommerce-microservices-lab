package com.teamsolution.lab.dto;

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
  private Long price;
  private String imageUrl;
  private ProductVariantInventoryDto inventory;
}
