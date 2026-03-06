package com.teamsolution.lab.service;

import com.teamsolution.lab.dto.CategoryDto;
import java.util.List;

public interface CategoryService {
  List<CategoryDto> getActiveCategoryTree();

  CategoryDto getActiveCategoryBySlug(String slug);
}
