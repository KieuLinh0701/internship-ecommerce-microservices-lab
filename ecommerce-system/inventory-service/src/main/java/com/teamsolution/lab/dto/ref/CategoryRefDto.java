package com.teamsolution.lab.dto.ref;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRefDto {
  private UUID id;
  private String name;
  private String slug;
}
