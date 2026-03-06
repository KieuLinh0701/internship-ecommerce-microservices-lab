package com.teamsolution.lab.dto.ref;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CategoryRefDto {
    private UUID id;
    private String name;
}

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BrandRefDto {
    private UUID id;
    private String name;
}
