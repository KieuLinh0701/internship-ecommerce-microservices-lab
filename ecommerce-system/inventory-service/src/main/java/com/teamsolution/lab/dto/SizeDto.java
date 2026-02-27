package com.teamsolution.lab.dto;

import com.teamsolution.lab.enums.SizeStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SizeDto {
  private UUID id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private UUID createdBy;
  private UUID updatedBy;
  private Boolean isDelete;
  private Long version;

  private String name;
  private String code;
  private Integer sortOrder;
  private SizeStatus status;
}
