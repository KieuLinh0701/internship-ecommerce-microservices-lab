package com.teamsolution.lab.mapper;

import com.teamsolution.lab.dto.ProductListDto;
import com.teamsolution.lab.entity.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = {ProductVariantMapper.class})
public interface ProductMapper extends BaseMapper<Product, ProductListDto> {

  @Mapping(target = "categoryId", source = "category.id")
  @Mapping(target = "variants", source = "variants")
  ProductListDto toDto(Product entity);

  @Mapping(target = "category", ignore = true)
  @Mapping(target = "variants", ignore = true)
  Product toEntity(ProductListDto dto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "category", ignore = true)
  @Mapping(target = "variants", ignore = true)
  void updateEntityFromDto(ProductListDto dto, @MappingTarget Product entity);
}
