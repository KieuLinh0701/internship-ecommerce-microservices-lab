package com.teamsolution.lab.dto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailDto {
  private UUID id;
  private String name;
  private String description;
  private UUID categorySlug;
  private String categoryName;
  private String brandSlug;
  private String brandName;
  private String brandLogoUrl;
  private Long basePrice;
  private List<ProductVariantDto> variants;
}
