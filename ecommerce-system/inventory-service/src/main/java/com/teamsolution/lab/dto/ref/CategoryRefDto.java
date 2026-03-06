package com.teamsolution.lab.dto.ref;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRefDto {
    private UUID id;
    private String name;
    private String slug;
}
