package com.teamsolution.lab.service.impl;

import com.teamsolution.lab.dto.CategoryDto;
import com.teamsolution.lab.entity.Category;
import com.teamsolution.lab.exception.ResourceNotFoundException;
import com.teamsolution.lab.mapper.CategoryMapper;
import com.teamsolution.lab.repository.CategoryRepository;
import com.teamsolution.lab.service.CategoryService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryServiceImpl extends BaseServiceImpl<Category, CategoryDto, UUID>
    implements CategoryService {
  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
    super(categoryRepository, categoryMapper);
    this.categoryRepository = categoryRepository;
    this.categoryMapper = categoryMapper;
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoryDto> getActiveCategoryTree() {
    List<Category> categories = categoryRepository.findCategoriesByParentIdIsNullAndIsDeleteFalse();

    return categories.stream().map(this::mapToTree).toList();
  }

  @Override
  public CategoryDto getActiveCategoryBySlug(String slug) {
    Category category =
        categoryRepository
            .findByIsDeleteFalseAndSlug(slug)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found or deleted"));

    return mapToTree(category);
  }

  private CategoryDto mapToTree(Category category) {
    List<CategoryDto> children =
        category.getChildren().stream()
            .filter(child -> !child.getIsDelete())
            .map(this::mapToTree)
            .collect(Collectors.toList());

    return CategoryDto.builder()
        .id(category.getId())
        .name(category.getName())
        .slug(category.getSlug())
        .children(children)
        .build();
  }
}
