package com.teamsolution.lab.repository;

import com.teamsolution.lab.entity.Category;
import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends BaseRepository<Category, UUID> {
  List<Category> findByIsDeleteFalse();

  List<Category> findCategoriesBySlug(String slug);
}
