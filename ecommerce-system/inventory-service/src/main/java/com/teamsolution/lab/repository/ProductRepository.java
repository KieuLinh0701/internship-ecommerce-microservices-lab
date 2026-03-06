package com.teamsolution.lab.repository;

import com.teamsolution.lab.entity.Product;
import com.teamsolution.lab.enums.ProductStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends BaseRepository<Product, UUID> {

  Optional<Product> findBySlugAndIsDeleteFalseAndStatus(String slug, ProductStatus status);

  Optional<Product> findByIdAndIsDeleteFalseAndStatus(UUID id, ProductStatus status);

  List<Product> findByIsDeleteFalseAndStatus(ProductStatus status);
}
