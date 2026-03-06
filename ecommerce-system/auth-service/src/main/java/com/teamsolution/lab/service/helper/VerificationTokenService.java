package com.teamsolution.lab.service.helper;

import com.teamsolution.lab.config.properties.OtpSecurityProperties;
import com.teamsolution.lab.entity.Account;
import com.teamsolution.lab.entity.VerificationToken;
import com.teamsolution.lab.enums.VerificationTokenType;
import com.teamsolution.lab.exception.InvalidTokenException;
import com.teamsolution.lab.exception.ResourceNotFoundException;
import com.teamsolution.lab.repository.VerificationTokenRepository;
import com.teamsolution.lab.util.OtpUtils;
import com.teamsolution.lab.util.UuidGenerator;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

  private final OtpSecurityProperties otpSecurityProperties;
  private final VerificationTokenRepository verificationTokenRepository;

  public String generateAndSave(Account account, VerificationTokenType type) {

    verificationTokenRepository.markOldOtpUsed(account.getId(), type);

    String rawOtp = RandomStringUtils.randomNumeric(6);

    VerificationToken token =
        VerificationToken.builder()
            .id(UuidGenerator.generate())
            .account(account)
            .token(OtpUtils.hashOtp(rawOtp))
            .type(type)
            .expiresAt(
                LocalDateTime.now()
                    .plusSeconds(otpSecurityProperties.getVerificationExpiresInSeconds()))
            .isUsed(false)
            .build();

    verificationTokenRepository.save(token);

    return rawOtp;
  }

  public VerificationToken verifyOtp(UUID accountId, String rawOtp, VerificationTokenType type) {
    VerificationToken verificationToken =
        verificationTokenRepository
            .findByAccountIdAndTokenAndTypeAndIsUsedFalse(accountId, OtpUtils.hashOtp(rawOtp), type)
            .orElseThrow(() -> new ResourceNotFoundException("Invalid or used token"));

    if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new InvalidTokenException("Token expired");
    }

    verificationToken.setUsed(true);
    verificationTokenRepository.save(verificationToken);
    return verificationToken;
  }

  public VerificationToken findActiveToken(UUID accountId, VerificationTokenType type) {
    return verificationTokenRepository
        .findActiveToken(accountId, type)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "No active "
                        + type.name().toLowerCase().replace("_", " ")
                        + " request found."));
  }

  public void save(VerificationToken token) {
    verificationTokenRepository.save(token);
  }
}
