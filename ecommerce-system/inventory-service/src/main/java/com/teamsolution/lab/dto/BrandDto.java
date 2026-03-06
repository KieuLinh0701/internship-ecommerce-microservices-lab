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
public class BrandDto {
  private UUID id;
  private String name;
  private String slug;
  private String logoUrl;
  private String description;
}
