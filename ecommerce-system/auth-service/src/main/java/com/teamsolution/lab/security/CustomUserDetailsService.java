package com.teamsolution.lab.security;

import com.teamsolution.lab.entity.Account;
import com.teamsolution.lab.enums.AccountRoleStatus;
import com.teamsolution.lab.enums.AccountStatus;
import com.teamsolution.lab.enums.RoleStatus;
import com.teamsolution.lab.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final AccountRepository accountRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Account account =
        accountRepository
            .findByEmailWithActiveRoles(
                email, AccountStatus.ACTIVE, AccountRoleStatus.ACTIVE, RoleStatus.ACTIVE)
            .orElseThrow(() -> new UsernameNotFoundException("User not found or inactive"));

    return new CustomUserDetails(account);
  }
}
