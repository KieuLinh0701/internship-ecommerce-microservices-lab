package com.teamsolution.lab.security;

import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

  private SecurityUtils() {}

  public static UUID getCurrentAccountId() {
    return UUID.fromString((String) getAuthentication().getPrincipal());
  }

  public static String getCurrentRole() {
    return getAuthentication().getAuthorities().stream()
        .findFirst()
        .map(GrantedAuthority::getAuthority)
        .orElse(null);
  }

  private static Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }
}
