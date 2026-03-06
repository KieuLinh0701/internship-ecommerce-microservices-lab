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
public class ProductImageDto {
  private UUID id;
  private String imageUrl;
  private Integer sortOrder;
  private Boolean isThumbnail;
}
