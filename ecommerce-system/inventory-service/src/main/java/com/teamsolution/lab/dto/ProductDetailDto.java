package com.teamsolution.lab.dto;

import com.teamsolution.lab.enums.ProductStatus;
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
  private UUID categoryId;
  private Long basePrice;
  private ProductStatus status;
  private List<ProductVariantDto> variants;
}
