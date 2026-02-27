package com.teamsolution.lab.controller;

import com.teamsolution.lab.dto.CategoryDto;
import com.teamsolution.lab.response.ApiResponse;
import com.teamsolution.lab.service.CategoryService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController extends BaseController<CategoryDto, UUID> {

  private final CategoryService categoryService;

  protected CategoryController(CategoryService categoryService) {
    super(categoryService);
    this.categoryService = categoryService;
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<CategoryDto>>> getActiveCategoryTree() {
    return ResponseEntity.ok(ApiResponse.success(categoryService.getActiveCategoryTree()));
  }

  @GetMapping("/slug/{slug}")
  public ResponseEntity<ApiResponse<List<CategoryDto>>> getActiveCategoryBySlug(
      @PathVariable String slug) {
    return ResponseEntity.ok(ApiResponse.success(categoryService.getActiveCategoryBySlug(slug)));
  }
}
