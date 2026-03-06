package com.teamsolution.lab.service;

import com.teamsolution.lab.entity.ProductVariant;

import java.util.UUID;

public interface ProductVariantService {

    public ProductVariant findByIdAndProductId(UUID productId, UUID variantId);

}
