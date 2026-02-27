package com.teamsolution.lab.service.impl;

import com.teamsolution.lab.dto.ProductListDto;
import com.teamsolution.lab.dto.request.ProductFilterRequest;
import com.teamsolution.lab.entity.Product;
import com.teamsolution.lab.enums.ProductStatus;
import com.teamsolution.lab.exception.ResourceNotFoundException;
import com.teamsolution.lab.mapper.ProductMapper;
import com.teamsolution.lab.repository.CategoryRepository;
import com.teamsolution.lab.repository.ProductRepository;
import com.teamsolution.lab.service.ProductService;
import com.teamsolution.lab.specification.ProductSpecification;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductServiceImpl extends BaseServiceImpl<Product, ProductListDto, UUID>
    implements ProductService {
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final CategoryRepository categoryRepository;

  public ProductServiceImpl(
      ProductRepository productRepository,
      ProductMapper productMapper,
      CategoryRepository categoryRepository) {
    super(productRepository, productMapper);
    this.productRepository = productRepository;
    this.productMapper = productMapper;
    this.categoryRepository = categoryRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductListDto> getByCategoryId(UUID categoryId) {
    if (!categoryRepository.existsById(categoryId)) {
      throw new ResourceNotFoundException("Category with ID " + categoryId + " does not exist.");
    }
    return productMapper.toDtoList(productRepository.findByCategoryIdAndIsDeleteFalse(categoryId));
  }

  @Override
  public Page<ProductListDto> getProducts(Pageable pageable, ProductFilterRequest filterRequest) {
    Page<Product> products =
        productRepository.findAll(ProductSpecification.build(filterRequest), pageable);
    return products.map(productMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public ProductListDto getById(UUID id) {
    Product product =
        productRepository
            .findByIdAndIsDeleteFalseAndStatus(id, ProductStatus.ACTIVE)
            .orElseThrow(
                () -> new ResourceNotFoundException("Product with ID " + id + " not found."));
    return productMapper.toDto(product);
  }
}
