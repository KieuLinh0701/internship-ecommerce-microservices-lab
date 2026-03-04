package com.teamsolution.lab.mapper;

import com.teamsolution.lab.dto.ProductVariantDto;
import com.teamsolution.lab.entity.ProductVariant;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductVariantMapper extends BaseMapper<ProductVariant, ProductVariantDto> {

  @Mapping(target = "inventory", source = "inventory")
  ProductVariantDto toDto(ProductVariant entity);

  @Mapping(target = "product", ignore = true)
  ProductVariant toEntity(ProductVariantDto dto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "product", ignore = true)
  @Mapping(target = "inventory", ignore = true)
  void updateEntityFromDto(ProductVariantDto dto, @MappingTarget ProductVariant entity);
}
