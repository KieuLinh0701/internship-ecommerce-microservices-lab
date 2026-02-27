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
public class CategoryDto {
  private UUID id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String name;
  private String slug;
  private UUID parentId;
  private List<CategoryDto> children;
  private UUID createdBy;
  private UUID updatedBy;
  private Boolean isDelete;
  private Long version;
}
