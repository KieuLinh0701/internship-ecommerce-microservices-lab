package com.teamsolution.lab.repository;

import com.teamsolution.lab.entity.Account;
import com.teamsolution.lab.enums.AccountRoleStatus;
import com.teamsolution.lab.enums.AccountStatus;
import com.teamsolution.lab.enums.RoleStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends BaseRepository<Account, UUID> {
  Optional<Account> findByEmail(String email);

  boolean existsByEmail(String email);

  @Query(
      """
          SELECT DISTINCT a
          FROM Account a
          LEFT JOIN FETCH a.accountRoles ar
          LEFT JOIN FETCH ar.role r
          WHERE a.email = :email
            AND a.status = :accountStatus
            AND ar.status = :accountRoleStatus
            AND r.status = :roleStatus
      """)
  Optional<Account> findByEmailWithActiveRoles(
      @Param("email") String email,
      @Param("accountStatus") AccountStatus accountStatus,
      @Param("accountRoleStatus") AccountRoleStatus accountRoleStatus,
      @Param("roleStatus") RoleStatus roleStatus);
}
