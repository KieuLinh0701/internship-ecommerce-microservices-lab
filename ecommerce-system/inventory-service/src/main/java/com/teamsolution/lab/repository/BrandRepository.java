package com.teamsolution.lab.repository;

import com.teamsolution.lab.entity.Brand;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BrandRepository extends BaseRepository<Brand, UUID> {

  Page<Brand> findByIsDeleteFalseAndStatusIsTrueAndNameContainingIgnoreCase(
      String keyword, Pageable pageable);

  Page<Brand> findByIsDeleteFalseAndStatusIsTrue(Pageable pageable);

  Optional<Brand> findByIsDeleteFalseAndStatusIsTrueAndSlug(String slug);
}
