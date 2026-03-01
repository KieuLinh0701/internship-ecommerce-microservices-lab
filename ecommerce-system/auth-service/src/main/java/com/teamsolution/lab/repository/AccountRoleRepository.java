package com.teamsolution.lab.repository;

import com.teamsolution.lab.entity.AccountRole;
import com.teamsolution.lab.entity.Role;
import com.teamsolution.lab.enums.AccountRoleStatus;
import com.teamsolution.lab.enums.RoleStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;
import java.util.UUID;

public interface AccountRoleRepository extends BaseRepository<AccountRole, UUID> {
    @Modifying
    @Query("UPDATE AccountRole ar SET ar.status = 'ACTIVE' WHERE ar.account.id = :accountId AND ar.status = 'PENDING'")
    void activateByAccountId(@Param("accountId") UUID accountId);

    @Query("""
        SELECT ar.role
        FROM AccountRole ar
        WHERE ar.account.id = :accountId
          AND ar.status = :accountRoleStatus
          AND ar.isDelete = false
          AND ar.role.status = :roleStatus
    """)
    Set<Role> findActiveRolesByAccountId(
            @Param("accountId") UUID accountId,
            @Param("accountRoleStatus") AccountRoleStatus accountRoleStatus,
            @Param("roleStatus") RoleStatus roleStatus
    );
}