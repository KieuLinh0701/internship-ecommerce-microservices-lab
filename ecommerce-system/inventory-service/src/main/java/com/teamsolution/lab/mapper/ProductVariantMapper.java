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
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = {ProductVariantInventoryMapper.class})
public interface ProductVariantMapper extends BaseMapper<ProductVariant, ProductVariantDto> {

  @Mapping(target = "productId", source = "product.id")
  @Mapping(target = "sizeId", source = "size.id")
  @Mapping(target = "inventory", source = "inventory")
  @Mapping(target = "createdBy", source = "createdBy")
  @Mapping(target = "updatedBy", source = "updatedBy")
  @Mapping(target = "isDelete", source = "isDelete")
  @Mapping(target = "version", source = "version")
  ProductVariantDto toDto(ProductVariant entity);

  @Mapping(target = "product", ignore = true)
  @Mapping(target = "size", ignore = true)
  @Mapping(target = "inventory", ignore = true)
  @Mapping(target = "createdBy", source = "createdBy")
  @Mapping(target = "updatedBy", source = "updatedBy")
  @Mapping(target = "isDelete", source = "isDelete")
  @Mapping(target = "version", source = "version")
  ProductVariant toEntity(ProductVariantDto dto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "product", ignore = true)
  @Mapping(target = "size", ignore = true)
  @Mapping(target = "inventory", ignore = true)
  @Mapping(target = "createdBy", source = "createdBy")
  @Mapping(target = "updatedBy", source = "updatedBy")
  @Mapping(target = "isDelete", source = "isDelete")
  @Mapping(target = "version", source = "version")
  void updateEntityFromDto(ProductVariantDto dto, @MappingTarget ProductVariant entity);
}
