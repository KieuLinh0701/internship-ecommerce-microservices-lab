package com.teamsolution.lab.repository;

import com.teamsolution.lab.entity.AccountRole;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface AccountRoleRepository extends BaseRepository<AccountRole, UUID> {
    @Modifying
    @Query("UPDATE AccountRole ar SET ar.status = 'ACTIVE' WHERE ar.account.id = :accountId AND ar.status = 'PENDING'")
    void activateByAccountId(@Param("accountId") UUID accountId);
}