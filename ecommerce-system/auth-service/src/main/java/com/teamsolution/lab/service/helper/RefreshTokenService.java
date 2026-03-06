package com.teamsolution.lab.service.helper;

import com.teamsolution.lab.config.properties.JwtProperties;
import com.teamsolution.lab.entity.Account;
import com.teamsolution.lab.entity.RefreshToken;
import com.teamsolution.lab.exception.InvalidTokenException;
import com.teamsolution.lab.exception.ResourceNotFoundException;
import com.teamsolution.lab.repository.RefreshTokenRepository;
import com.teamsolution.lab.util.OtpUtils;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtProperties jwtProperties;

  public void save(Account account, String rawRefreshToken) {
    RefreshToken refreshToken =
        RefreshToken.builder()
            .account(account)
            .token(OtpUtils.hashOtp(rawRefreshToken))
            .isUsed(false)
            .expiresAt(LocalDateTime.now().plusSeconds(jwtProperties.getRefreshTokenExpiration()))
            .build();
    refreshTokenRepository.save(refreshToken);
  }

  public RefreshToken validateAndGet(String rawToken) {
    RefreshToken storedToken =
        refreshTokenRepository
            .findByTokenAndIsUsedFalse(OtpUtils.hashOtp(rawToken))
            .orElseThrow(() -> new ResourceNotFoundException("Invalid or used refresh token"));

    if (storedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new InvalidTokenException("Refresh token expired");
    }

    return storedToken;
  }

  @Transactional
  public void markAllUsedByAccountId(UUID accountId) {
    refreshTokenRepository.markAllUsedByAccountId(accountId);
  }
}
