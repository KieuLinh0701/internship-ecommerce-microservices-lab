package com.teamsolution.lab.mapper;

import com.teamsolution.lab.dto.BrandListDto;
import com.teamsolution.lab.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BrandListMapper extends BaseMapper<Brand, BrandListDto> {
  @Override
  BrandListDto toDto(Brand entity);

  @Override
  Brand toEntity(BrandListDto dto);

  @Override
  void updateEntityFromDto(BrandListDto dto, @MappingTarget Brand entity);
}
