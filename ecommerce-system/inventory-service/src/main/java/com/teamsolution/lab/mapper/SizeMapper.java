package com.teamsolution.lab.mapper;

import com.teamsolution.lab.dto.ProductListDto;
import com.teamsolution.lab.dto.SizeDto;
import com.teamsolution.lab.entity.Product;
import com.teamsolution.lab.entity.Size;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SizeMapper extends BaseMapper<Product, ProductListDto> {
  @Mapping(target = "createdBy", source = "createdBy")
  @Mapping(target = "updatedBy", source = "updatedBy")
  @Mapping(target = "isDelete", source = "isDelete")
  @Mapping(target = "version", source = "version")
  SizeDto toDto(Size entity);

  @Mapping(target = "createdBy", source = "createdBy")
  @Mapping(target = "updatedBy", source = "updatedBy")
  @Mapping(target = "isDelete", source = "isDelete")
  @Mapping(target = "version", source = "version")
  Size toEntity(SizeDto dto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "createdBy", source = "createdBy")
  @Mapping(target = "updatedBy", source = "updatedBy")
  @Mapping(target = "isDelete", source = "isDelete")
  @Mapping(target = "version", source = "version")
  void updateEntityFromDto(SizeDto dto, @MappingTarget Size entity);
}
