package com.teamsolution.lab.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
  private UUID id;
  private UUID accountId;
  private String fullName;
  private Set<AddressDto> addresses;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private UUID createdBy;
  private UUID updatedBy;
  private Boolean isDelete;
  private Long version;
}
