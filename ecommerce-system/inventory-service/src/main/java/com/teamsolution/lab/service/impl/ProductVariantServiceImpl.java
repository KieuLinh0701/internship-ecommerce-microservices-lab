package com.teamsolution.lab.service.impl;

import com.teamsolution.lab.entity.ProductVariant;
import com.teamsolution.lab.exception.ResourceNotFoundException;
import com.teamsolution.lab.repository.ProductVariantRepository;
import com.teamsolution.lab.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {
  private final ProductVariantRepository productVariantRepository;

  @Override
  public ProductVariant findByIdAndProductId(UUID productId, UUID variantId) {
      return productVariantRepository.findByIdAndProductIdAndIsDeleteFalse(variantId, productId)
              .orElseThrow(() -> new ResourceNotFoundException("Product variant not found"));
  }
}
