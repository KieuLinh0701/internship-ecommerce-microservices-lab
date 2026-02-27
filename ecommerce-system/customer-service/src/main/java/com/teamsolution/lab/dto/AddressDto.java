package com.teamsolution.lab.dto;

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
public class AddressDto {
  private UUID id;
  private String name;
  private String phone;
  private Integer cityCode;
  private String cityName;
  private Integer wardCode;
  private String wardName;
  private String detail;
  private boolean isDefault;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private UUID createdBy;
  private UUID updatedBy;
  private Boolean isDelete;
  private Long version;
}
