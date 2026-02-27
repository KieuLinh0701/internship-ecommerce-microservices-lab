package com.teamsolution.lab.service;

import com.teamsolution.lab.dto.ProductListDto;
import com.teamsolution.lab.dto.request.ProductFilterRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService extends BaseService<ProductListDto, UUID> {
  List<ProductListDto> getByCategoryId(UUID categoryId);

  Page<ProductListDto> getProducts(Pageable pageable, ProductFilterRequest filterRequest);
}
