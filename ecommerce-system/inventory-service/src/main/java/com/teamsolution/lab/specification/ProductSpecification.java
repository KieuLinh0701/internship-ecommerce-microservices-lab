package com.teamsolution.lab.specification;

import com.teamsolution.lab.dto.request.ProductFilterRequest;
import com.teamsolution.lab.entity.Product;
import com.teamsolution.lab.enums.ProductStatus;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
  // Filter by keyword in name or description
  public static Specification<Product> hasKeyword(String keyword) {
    return (root, query, cb) -> {
      if (keyword == null || keyword.isEmpty()) {
        return cb.conjunction();
      }
      String likePattern = "%" + keyword.toLowerCase() + "%";
      return cb.or(
          cb.like(cb.lower(root.get("name")), likePattern),
          cb.like(cb.lower(root.get("description")), likePattern));
    };
  }

  // Filter by category slug
  public static Specification<Product> hasCategorySlug(String categorySlug) {
    return (root, query, cb) -> {
      if (categorySlug == null || categorySlug.isEmpty()) {
        return cb.conjunction();
      }
      return cb.equal(root.get("category").get("slug"), categorySlug);
    };
  }

  // Filter by brand slug
  public static Specification<Product> hasBrandSlug(String brandSlug) {
    return (root, query, cb) -> {
      if (brandSlug == null || brandSlug.isEmpty()) {
        return cb.conjunction();
      }
      return cb.equal(root.get("brand").get("slug"), brandSlug);
    };
  }

  // Filter by minimum price
  public static Specification<Product> hasMinPrice(Long minPrice) {
    return (root, query, cb) -> {
      if (minPrice == null) {
        return cb.conjunction();
      }
      return cb.greaterThanOrEqualTo(root.get("basePrice"), minPrice);
    };
  }

  // Filter by maximum price
  public static Specification<Product> hasMaxPrice(Long maxPrice) {
    return (root, query, cb) -> {
      if (maxPrice == null) {
        return cb.conjunction();
      }
      return cb.lessThanOrEqualTo(root.get("basePrice"), maxPrice);
    };
  }

  // Filter by size ID
  public static Specification<Product> hasSizeId(UUID sizeId) {
    return (root, query, cb) -> {
      if (sizeId == null) {
        return cb.conjunction();
      }
      return cb.equal(root.get("size").get("id"), sizeId);
    };
  }

  // Only active products
  public static Specification<Product> isActive() {
    return (root, query, cb) -> cb.equal(root.get("status"), ProductStatus.ACTIVE);
  }

  // Only products that are not deleted
  public static Specification<Product> notDeleted() {
    return (root, query, cb) -> cb.isFalse(root.get("isDelete"));
  }

  public static Specification<Product> build(ProductFilterRequest filterRequest) {
    return Specification.where(hasKeyword(filterRequest.getKeyword()))
        .and(hasCategorySlug(filterRequest.getCategorySlug()))
        .and(hasBrandSlug(filterRequest.getBrandSlug()))
        .and(hasMinPrice(filterRequest.getMinPrice()))
        .and(hasMaxPrice(filterRequest.getMaxPrice()))
        .and(hasSizeId(filterRequest.getSizeId()))
        .and(notDeleted())
        .and(isActive());
  }
}
