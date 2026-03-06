package com.teamsolution.lab.dto;

import com.teamsolution.lab.dto.ref.AttributeWithValuesDto;
import com.teamsolution.lab.dto.ref.BrandRefDto;
import com.teamsolution.lab.dto.ref.CategoryRefDto;
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
  private String slug;
  private String description;
  private Long basePrice;
  private ProductStatus status;
  private CategoryRefDto category;
  private BrandRefDto brand;
  private List<ProductImageDto> images;
  private List<AttributeWithValuesDto> attributes;
  private List<ProductVariantDto> variants;
}
