package com.teamsolution.lab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListDto {
    private UUID id;
    private String name;
    private String description;
    private String slug;
    private Long basePrice;
    private String thumbnail;
    private String categorySlug;
    private String brandSlug;
}
