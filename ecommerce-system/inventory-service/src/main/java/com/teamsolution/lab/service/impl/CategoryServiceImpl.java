package com.teamsolution.lab.service.impl;

import com.teamsolution.lab.dto.CategoryDto;
import com.teamsolution.lab.entity.Category;
import com.teamsolution.lab.mapper.CategoryMapper;
import com.teamsolution.lab.repository.CategoryRepository;
import com.teamsolution.lab.service.CategoryService;
import java.util.List;
import java.util.UUID;
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
    return categoryMapper.toDtoList(categoryRepository.findByIsDeleteFalse());
  }

  @Override
  public List<CategoryDto> getActiveCategoryBySlug(String slug) {
    return categoryMapper.toDtoList(categoryRepository.findCategoriesBySlug(slug));
  }
}
