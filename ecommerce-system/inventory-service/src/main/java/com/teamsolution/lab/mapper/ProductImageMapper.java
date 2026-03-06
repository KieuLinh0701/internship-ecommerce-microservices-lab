package com.teamsolution.lab.mapper;

import com.teamsolution.lab.dto.ProductImageDto;
import com.teamsolution.lab.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductImageMapper extends BaseMapper<ProductImage, ProductImageDto> {
  @Override
  ProductImageDto toDto(ProductImage entity);

  @Override
  ProductImage toEntity(ProductImageDto dto);

  @Override
  void updateEntityFromDto(ProductImageDto dto, @MappingTarget ProductImage entity);
}
