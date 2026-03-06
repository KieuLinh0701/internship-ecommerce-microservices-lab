package com.teamsolution.lab.mapper;

import com.teamsolution.lab.dto.ProductListDto;
import com.teamsolution.lab.entity.Product;
import com.teamsolution.lab.entity.ProductImage;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductListMapper extends BaseMapper<Product, ProductListDto> {

    @Mapping(target = "thumbnail", expression = "java(extractThumbnail(entity))")
    ProductListDto toDto(Product entity);

    default String extractThumbnail(Product product) {
        if (product.getImages() == null) return null;
        return product.getImages().stream()
                .filter(img -> Boolean.TRUE.equals(img.getIsThumbnail()))
                .map(ProductImage::getImageUrl)
                .findFirst()
                .orElse(null);
    }

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "images", ignore = true)
    Product toEntity(ProductListDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "images", ignore = true)
    void updateEntityFromDto(ProductListDto dto, @MappingTarget Product entity);
}