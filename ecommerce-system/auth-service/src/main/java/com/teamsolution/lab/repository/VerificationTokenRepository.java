package com.teamsolution.lab.repository;

import com.teamsolution.lab.entity.VerificationToken;
import com.teamsolution.lab.enums.VerificationTokenType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenRepository extends BaseRepository<VerificationToken, UUID> {
  @Modifying
  @Transactional
  @Query(
      "UPDATE VerificationToken v SET v.isUsed = true WHERE v.account.id = :accountId AND v.type = :type AND v.isUsed = false")
  void markOldOtpUsed(
      @Param("accountId") UUID accountId, @Param("type") VerificationTokenType type);

  Optional<VerificationToken> findByAccountIdAndTokenAndTypeAndIsUsedFalse(
      UUID accountId, String token, VerificationTokenType type);
}
