package com.teamsolution.lab.dto;

import com.teamsolution.lab.enums.ProductStatus;
import java.time.LocalDateTime;
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
public class ProductListDto {
  private UUID id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String name;
  private String description;
  private UUID categoryId;
  private Long basePrice;
  private ProductStatus status;
  private List<ProductVariantDto> variants;
  private UUID createdBy;
  private UUID updatedBy;
  private Boolean isDelete;
  private Long version;
}
