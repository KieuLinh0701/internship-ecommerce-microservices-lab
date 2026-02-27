package com.teamsolution.lab.controller;

import com.teamsolution.lab.dto.ProductListDto;
import com.teamsolution.lab.dto.request.ProductFilterRequest;
import com.teamsolution.lab.response.ApiResponse;
import com.teamsolution.lab.response.PageResponse;
import com.teamsolution.lab.service.ProductService;
import com.teamsolution.lab.util.PageableUtils;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController extends BaseController<ProductListDto, UUID> {

  private final ProductService productService;

  protected ProductController(ProductService productService) {
    super(productService);
    this.productService = productService;
  }

  @GetMapping
  public ResponseEntity<ApiResponse<PageResponse<ProductListDto>>> getProducts(
      ProductFilterRequest filterRequest) {
    Pageable pageable =
        (Pageable)
            PageableUtils.toPageable(
                filterRequest.getPage(),
                filterRequest.getSize(),
                filterRequest.getSortBy(),
                filterRequest.getDirection());

    Page<ProductListDto> result = productService.getProducts(pageable, filterRequest);
    return ResponseEntity.ok(ApiResponse.success(PageResponse.from(result)));
  }

  @Override
  public ResponseEntity<ApiResponse<ProductListDto>> getById(UUID id) {
    ProductListDto product = productService.getById(id);
    return ResponseEntity.ok(ApiResponse.success(product));
  }
}
