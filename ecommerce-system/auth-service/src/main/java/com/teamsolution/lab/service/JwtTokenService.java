package com.teamsolution.lab.service;

import com.teamsolution.lab.config.properties.JwtProperties;
import com.teamsolution.lab.dto.response.AuthResponse;
import com.teamsolution.lab.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;
  private final JwtProperties jwtProperties;

  public AuthResponse generateTokens(Account account) {
    Instant now = Instant.now();

    JwtClaimsSet accessClaims =
        JwtClaimsSet.builder()
            .issuer(jwtProperties.getIssuer())
            .subject(account.getId().toString())
            .issuedAt(now)
            .expiresAt(now.plusSeconds(jwtProperties.getAccessTokenExpiration()))
            .claim("account_id", account.getId().toString())
            .claim("email", account.getEmail())
            .claim(
                "roles",
                account.getAccountRoles().stream().map(ar -> ar.getRole().getName()).toList())
            .build();

    JwtClaimsSet refreshClaims =
        JwtClaimsSet.builder()
            .issuer(jwtProperties.getIssuer())
            .subject(account.getId().toString())
            .issuedAt(now)
            .expiresAt(now.plusSeconds(jwtProperties.getRefreshTokenExpiration()))
            .build();

    JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).build();

    String accessToken =
        jwtEncoder.encode(JwtEncoderParameters.from(header, accessClaims)).getTokenValue();

    String refreshToken =
        jwtEncoder.encode(JwtEncoderParameters.from(header, refreshClaims)).getTokenValue();

    return new AuthResponse(accessToken, refreshToken);
  }

  public String extractSubject(String token) {
    return jwtDecoder.decode(token).getSubject();
  }
}
