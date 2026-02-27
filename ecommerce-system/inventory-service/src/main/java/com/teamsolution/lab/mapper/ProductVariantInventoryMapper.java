package com.teamsolution.lab.mapper;

import com.teamsolution.lab.dto.ProductVariantInventoryDto;
import com.teamsolution.lab.entity.ProductVariantInventory;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductVariantInventoryMapper
    extends BaseMapper<ProductVariantInventory, ProductVariantInventoryDto> {

  @Mapping(target = "variantId", source = "variant.id")
  @Mapping(target = "createdBy", source = "createdBy")
  @Mapping(target = "updatedBy", source = "updatedBy")
  @Mapping(target = "isDelete", source = "isDelete")
  @Mapping(target = "version", source = "version")
  ProductVariantInventoryDto toDto(ProductVariantInventory entity);

  @Mapping(target = "variant", ignore = true)
  @Mapping(target = "createdBy", source = "createdBy")
  @Mapping(target = "updatedBy", source = "updatedBy")
  @Mapping(target = "isDelete", source = "isDelete")
  @Mapping(target = "version", source = "version")
  ProductVariantInventory toEntity(ProductVariantInventoryDto dto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "variant", ignore = true)
  @Mapping(target = "createdBy", source = "createdBy")
  @Mapping(target = "updatedBy", source = "updatedBy")
  @Mapping(target = "isDelete", source = "isDelete")
  @Mapping(target = "version", source = "version")
  void updateEntityFromDto(
      ProductVariantInventoryDto dto, @MappingTarget ProductVariantInventory entity);
}
