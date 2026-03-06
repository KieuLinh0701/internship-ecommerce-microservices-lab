package com.teamsolution.lab.controller;

import com.teamsolution.lab.dto.ProductDetailDto;
import com.teamsolution.lab.dto.ProductListDto;
import com.teamsolution.lab.dto.ProductVariantDto;
import com.teamsolution.lab.dto.request.ProductFilterRequest;
import com.teamsolution.lab.response.ApiResponse;
import com.teamsolution.lab.response.PageResponse;
import com.teamsolution.lab.service.ProductService;
import com.teamsolution.lab.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public ResponseEntity<ApiResponse<PageResponse<ProductListDto>>> getProducts(
      ProductFilterRequest filterRequest) {
    Pageable pageable = PageableUtils.toPageable(
            filterRequest.getPage(),
            filterRequest.getSize(),
            filterRequest.getSortBy(),
            filterRequest.getDirection());

    Page<ProductListDto> products = productService.getProducts(pageable, filterRequest);
    return ResponseEntity.ok(ApiResponse.success(PageResponse.from(products)));
  }

  @GetMapping("/{slug}")
  public ResponseEntity<ApiResponse<ProductDetailDto>> getBySlug(
          @PathVariable String slug
  ) {
      ProductDetailDto product = productService.getProductBySlug(slug);
    return ResponseEntity.ok(ApiResponse.success(product));
  }

    @GetMapping("/{productId}/variants/{variantId}")
    public ResponseEntity<ApiResponse<ProductVariantDto>> getVariantById(
            @PathVariable UUID productId,
            @PathVariable UUID variantId
    ) {

        ProductVariantDto variant = productService.getVariantById(productId, variantId);
        return ResponseEntity.ok(ApiResponse.success(variant));
    }
}
