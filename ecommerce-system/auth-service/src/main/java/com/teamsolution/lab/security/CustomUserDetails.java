package com.teamsolution.lab.security;

import com.teamsolution.lab.entity.Account;
import com.teamsolution.lab.entity.AccountRole;
import com.teamsolution.lab.enums.AccountStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record CustomUserDetails(Account account) implements UserDetails {

  public UUID getAccountId() {
    return account.getId();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<AccountRole> accountRoles = account.getAccountRoles();

    if (accountRoles == null) {
      return Set.of();
    }

    return accountRoles.stream()
        .map(ar -> new SimpleGrantedAuthority(ar.getRole().getName()))
        .collect(Collectors.toSet());
  }

  @Override
  public String getPassword() {
    return account.getPassword();
  }

  @Override
  public String getUsername() {
    return account.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return account.getStatus() == AccountStatus.PENDING;
  }
}
