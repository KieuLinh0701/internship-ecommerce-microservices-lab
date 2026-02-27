package com.teamsolution.lab.service;

import com.teamsolution.lab.dto.CategoryDto;
import java.util.List;
import java.util.UUID;

public interface CategoryService extends BaseService<CategoryDto, UUID> {
  List<CategoryDto> getActiveCategoryTree();

  List<CategoryDto> getActiveCategoryBySlug(String slug);
}
