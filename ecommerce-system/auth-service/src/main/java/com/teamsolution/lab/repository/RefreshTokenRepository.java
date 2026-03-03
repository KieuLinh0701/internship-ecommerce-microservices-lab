package com.teamsolution.lab.repository;

import com.teamsolution.lab.entity.RefreshToken;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends BaseRepository<RefreshToken, UUID> {
  Optional<RefreshToken> findByTokenAndIsUsedFalse(String token);

  @Modifying
  @Query(
      "UPDATE RefreshToken rt SET rt.isUsed = true WHERE rt.account.id = :accountId AND rt.isUsed = false")
  void markAllUsedByAccountId(@Param("accountId") UUID accountId);
}
