package com.teamsolution.lab.service.helper;

import com.teamsolution.lab.config.properties.JwtProperties;
import com.teamsolution.lab.dto.response.AuthResponse;
import com.teamsolution.lab.entity.Account;
import com.teamsolution.lab.enums.SystemRole;
import com.teamsolution.lab.exception.NoRecordsFoundException;
import com.teamsolution.lab.exception.RoleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;
  private final JwtProperties jwtProperties;
  private final AccountService accountService;

  public AuthResponse generateTokens(Account account, String selectedRole) {
    Instant now = Instant.now();

    Set<String> roles = accountService.getActiveRoleNamesByAccountId(account.getId());

    if (roles.isEmpty()) {
      throw new NoRecordsFoundException("Account has no active roles");
    }

    String currentRole;
    if (selectedRole == null || selectedRole.isBlank()) {
      String defaultRole = SystemRole.CUSTOMER.name();

       currentRole = roles.contains(defaultRole)
              ? defaultRole
              : roles.iterator().next();
    } else {
      if (!roles.contains(selectedRole)) {
        throw new RoleNotFoundException("Invalid role selected");
      }

      currentRole = selectedRole;
    }


    JwtClaimsSet accessClaims =
        JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(account.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(jwtProperties.getAccessTokenExpiration()))
                .claim("account_id", account.getId().toString())
                .claim("email", account.getEmail())
                .claim("roles", roles)
                .claim("current_role", currentRole)
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
