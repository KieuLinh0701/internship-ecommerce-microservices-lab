package com.teamsolution.lab.service.impl;

import com.teamsolution.lab.dto.CategoryDto;
import com.teamsolution.lab.entity.Category;
import com.teamsolution.lab.exception.ResourceNotFoundException;
import com.teamsolution.lab.repository.CategoryRepository;
import com.teamsolution.lab.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;

  @Override
  @Transactional(readOnly = true)
  public List<CategoryDto> getActiveCategoryTree() {
    List<Category> categories = categoryRepository.findCategoriesByParentIdIsNullAndIsDeleteFalse();

    return categories.stream().map(this::mapToTree).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public CategoryDto getActiveCategoryBySlug(String slug) {
    Category category =
        categoryRepository
            .findByIsDeleteFalseAndSlug(slug)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

    return mapToTree(category);
  }

  private CategoryDto mapToTree(Category category) {
    List<CategoryDto> children =
        category.getChildren().stream()
            .filter(child -> !child.getIsDelete())
            .map(this::mapToTree)
            .toList();

    return CategoryDto.builder()
        .id(category.getId())
        .name(category.getName())
        .slug(category.getSlug())
        .children(children)
        .build();
  }
}
