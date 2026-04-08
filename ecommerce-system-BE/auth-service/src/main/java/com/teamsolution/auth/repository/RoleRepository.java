package com.teamsolution.auth.repository;

import com.teamsolution.auth.entity.Role;
import com.teamsolution.common.core.enums.auth.RoleStatus;
import com.teamsolution.common.jpa.repository.BaseRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepository extends BaseRepository<Role, UUID>, JpaSpecificationExecutor<Role> {

  Optional<Role> findByNameAndStatusAndIsDeletedFalse(String name, RoleStatus status);

  boolean existsByNameIgnoreCase(String name);

  Optional<Role> findByIdAndIsDeletedFalse(UUID id);
}
