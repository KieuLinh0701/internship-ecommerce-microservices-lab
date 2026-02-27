package com.teamsolution.lab.mapper;

import com.teamsolution.lab.dto.BrandDto;
import com.teamsolution.lab.entity.Brand;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BrandMapper extends BaseMapper<Brand, BrandDto> {
  @Mapping(target = "products", source = "products")
  BrandDto toDto(Brand entity);

  @Mapping(target = "products", ignore = true)
  Brand toEntity(BrandDto dto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "products", ignore = true)
  void updateEntityFromDto(BrandDto dto, @MappingTarget Brand entity);
}
