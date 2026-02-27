package com.teamsolution.lab.dto;

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
public class BrandDto {
  private UUID id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private UUID createdBy;
  private UUID updatedBy;
  private Boolean isDelete;
  private Long version;

  private String name;
  private String slug;
  private String logoUrl;
  private boolean status;
  private String description;
  private List<ProductListDto> products;
}
