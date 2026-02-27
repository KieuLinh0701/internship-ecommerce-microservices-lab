package com.teamsolution.lab.repository;

import com.teamsolution.lab.entity.Role;
import com.teamsolution.lab.enums.RoleStatus;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends BaseRepository<Role, UUID> {
  Optional<Role> findByNameAndStatusAndIsDelete(String name, RoleStatus status, Boolean isDelete);
}
