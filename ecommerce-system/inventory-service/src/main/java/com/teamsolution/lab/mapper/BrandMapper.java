package com.teamsolution.lab.mapper;

import com.teamsolution.lab.dto.BrandDto;
import com.teamsolution.lab.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BrandMapper extends BaseMapper<Brand, BrandDto> {
  @Override
  BrandDto toDto(Brand entity);

  @Override
  Brand toEntity(BrandDto dto);

  @Override
  void updateEntityFromDto(BrandDto dto, @MappingTarget Brand entity);
}
