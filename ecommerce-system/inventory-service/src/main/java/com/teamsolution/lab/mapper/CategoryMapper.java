package com.teamsolution.lab.mapper;

import com.teamsolution.lab.dto.CategoryDto;
import com.teamsolution.lab.entity.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper extends BaseMapper<Category, CategoryDto> {
  @Mapping(target = "parentId", source = "parent.id")
  @Mapping(target = "children", source = "children")
  @Mapping(target = "createdBy", source = "createdBy")
  @Mapping(target = "updatedBy", source = "updatedBy")
  @Mapping(target = "isDelete", source = "isDelete")
  @Mapping(target = "version", source = "version")
  CategoryDto toDto(Category entity);

  @Mapping(target = "parent", ignore = true)
  @Mapping(target = "children", ignore = true)
  @Mapping(target = "createdBy", source = "createdBy")
  @Mapping(target = "updatedBy", source = "updatedBy")
  @Mapping(target = "isDelete", source = "isDelete")
  @Mapping(target = "version", source = "version")
  Category toEntity(CategoryDto dto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "parent", ignore = true)
  @Mapping(target = "children", ignore = true)
  @Mapping(target = "createdBy", source = "createdBy")
  @Mapping(target = "updatedBy", source = "updatedBy")
  @Mapping(target = "isDelete", source = "isDelete")
  @Mapping(target = "version", source = "version")
  void updateEntityFromDto(CategoryDto dto, @MappingTarget Category entity);
}
