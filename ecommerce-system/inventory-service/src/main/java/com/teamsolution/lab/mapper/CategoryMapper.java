package com.teamsolution.lab.mapper;

import com.teamsolution.lab.dto.CategoryDto;
import com.teamsolution.lab.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper extends BaseMapper<Category, CategoryDto> {
  @Mapping(target = "children", source = "children")
  CategoryDto toDto(Category entity);

  @Mapping(target = "children", ignore = true)
  Category toEntity(CategoryDto dto);

  void updateEntityFromDto(CategoryDto dto, @MappingTarget Category entity);
}
