package com.teamsolution.lab.repository;

import com.teamsolution.lab.entity.Product;
import com.teamsolution.lab.enums.ProductStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends BaseRepository<Product, UUID> {
  List<Product> findByCategoryIdAndIsDeleteFalse(UUID categoryId);

  Optional<Product> findByIdAndIsDeleteFalseAndStatus(UUID uuid, ProductStatus status);
}
