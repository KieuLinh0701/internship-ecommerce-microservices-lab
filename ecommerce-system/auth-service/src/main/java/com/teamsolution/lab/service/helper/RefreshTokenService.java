package com.teamsolution.lab.service.helper;

import com.teamsolution.lab.config.properties.JwtProperties;
import com.teamsolution.lab.entity.Account;
import com.teamsolution.lab.entity.RefreshToken;
import com.teamsolution.lab.exception.InvalidTokenException;
import com.teamsolution.lab.exception.ResourceNotFoundException;
import com.teamsolution.lab.repository.RefreshTokenRepository;
import com.teamsolution.lab.util.OtpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    public void save(Account account, String rawRefreshToken) {
        String hashedToken = OtpUtils.hashOtp(rawRefreshToken);
        RefreshToken refreshToken = RefreshToken.builder()
                .account(account)
                .token(hashedToken)
                .isUsed(false)
                .expiresAt(LocalDateTime.now().plusSeconds(jwtProperties.getRefreshTokenExpiration()))
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validateAndGet(String rawToken) {
        String hashedToken = OtpUtils.hashOtp(rawToken);

        RefreshToken storedToken = refreshTokenRepository
                .findByTokenAndIsUsedFalse(hashedToken)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or used refresh token"));

        if (storedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Refresh token expired");
        }

        return storedToken;
    }
}
