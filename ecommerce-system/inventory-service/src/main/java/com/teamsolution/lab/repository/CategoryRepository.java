package com.teamsolution.lab.repository;

import com.teamsolution.lab.entity.Category;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends BaseRepository<Category, UUID> {

  List<Category> findCategoriesByParentIdIsNullAndIsDeleteFalse();

  Optional<Category> findByIsDeleteFalseAndSlug(String slug);
}
