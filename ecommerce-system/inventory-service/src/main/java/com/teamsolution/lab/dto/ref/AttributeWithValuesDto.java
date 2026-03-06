package com.teamsolution.lab.dto.ref;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeWithValuesDto {
    private UUID id;
    private String name;
    private List<AttributeValueRefDto> values;
}