package com.teamsolution.lab.repository;

import com.teamsolution.lab.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface BrandRepository extends BaseRepository<Brand, UUID> {

    Page<Brand> findByIsDeleteFalseAndStatusIsTrueAndNameContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Brand> findByIsDeleteFalseAndStatusIsTrue(Pageable pageable);

    Optional<Brand> findByIsDeleteFalseAndStatusIsTrueAndSlug(String slug);
}
