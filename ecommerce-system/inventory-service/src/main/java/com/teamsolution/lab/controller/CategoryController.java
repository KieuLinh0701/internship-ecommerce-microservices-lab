package com.teamsolution.lab.controller;

import com.teamsolution.lab.dto.CategoryDto;
import com.teamsolution.lab.response.ApiResponse;
import com.teamsolution.lab.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<CategoryDto>>> getActiveCategoryTree() {
    List<CategoryDto> categoryDtos = categoryService.getActiveCategoryTree();
    return ResponseEntity.ok(ApiResponse.success(categoryDtos));
  }

  @GetMapping("/{slug}")
  public ResponseEntity<ApiResponse<CategoryDto>> getActiveCategoryBySlug(
      @PathVariable String slug) {
    CategoryDto categoryDto = categoryService.getActiveCategoryBySlug(slug);
    return ResponseEntity.ok(ApiResponse.success(categoryDto));
  }
}
