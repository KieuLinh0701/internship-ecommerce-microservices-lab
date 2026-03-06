package com.teamsolution.lab.repository;

import com.teamsolution.lab.entity.ProductVariant;

import java.util.Optional;
import java.util.UUID;

public interface ProductVariantRepository extends BaseRepository<ProductVariant, UUID> {
    Optional<ProductVariant> findByIdAndProductIdAndIsDeleteFalse(UUID id, UUID productId);
}
