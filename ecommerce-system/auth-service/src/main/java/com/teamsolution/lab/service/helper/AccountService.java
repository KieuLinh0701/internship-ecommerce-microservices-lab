package com.teamsolution.lab.service.helper;

import com.teamsolution.lab.entity.Account;
import com.teamsolution.lab.entity.AccountRole;
import com.teamsolution.lab.entity.AccountRoleId;
import com.teamsolution.lab.entity.Role;
import com.teamsolution.lab.enums.AccountRoleStatus;
import com.teamsolution.lab.enums.AccountStatus;
import com.teamsolution.lab.enums.RoleStatus;
import com.teamsolution.lab.exception.DuplicateResourceException;
import com.teamsolution.lab.exception.ResourceNotFoundException;
import com.teamsolution.lab.grpc.CustomerGrpcClient;
import com.teamsolution.lab.repository.AccountRepository;
import com.teamsolution.lab.repository.AccountRoleRepository;
import com.teamsolution.lab.repository.RoleRepository;
import com.teamsolution.lab.util.UuidGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final RoleRepository roleRepository;
    private final CustomerGrpcClient customerGrpcClient;

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    public Account findById(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    public void checkEmailNotExists(String email) {
        if (accountRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email already exists");
        }
    }

    private Account getAccountOrThrow(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    public Account getAccountWithActiveRoles(String email) {
        return accountRepository.findByEmailWithActiveRoles(
                        email,
                        AccountStatus.ACTIVE,
                        AccountRoleStatus.ACTIVE,
                        RoleStatus.ACTIVE
                )
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    public Set<String> getActiveRoleNamesByAccountId(UUID accountId) {

        Account account = getAccountOrThrow(accountId);

        Set<Role> roles = accountRoleRepository.findActiveRolesByAccountId(
                account.getId(),
                AccountRoleStatus.ACTIVE,
                RoleStatus.ACTIVE
        );

        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    @Transactional
    public Account createNewAccount(String email,
                                    String hashPassword,
                                    AccountStatus accountStatus,
                                    AccountRoleStatus accountRoleStatus,
                                    String fullName,
                                    String phone,
                                    String avatarUrl) {
        Account newAccount =
                Account.builder()
                        .id(UuidGenerator.generate())
                        .email(email)
                        .password(hashPassword)
                        .status(accountStatus)
                        .build();
        Account saved = accountRepository.save(newAccount);

        Role userRole =
                roleRepository
                        .findByNameAndStatusAndIsDelete("CUSTOMER", RoleStatus.ACTIVE, false)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Role not found or inactive or deleted"));

        AccountRole accountRole =
                AccountRole.builder()
                        .id(new AccountRoleId(saved.getId(), userRole.getId()))
                        .account(saved)
                        .role(userRole)
                        .status(accountRoleStatus)
                        .build();
        accountRoleRepository.save(accountRole);

        // Create customer
        customerGrpcClient.createNewCustomer(
                saved.getId(),
                fullName,
                phone,
                avatarUrl
        );

        return saved;
    }

    @Transactional
    public void findOrCreateGoogleAccount(String email,
                                          String fullName,
                                          String avatarUrl) {
        accountRepository.findByEmail(email)
                .orElseGet(() -> createNewAccount(
                        email,
                        "",
                        AccountStatus.ACTIVE,
                        AccountRoleStatus.ACTIVE,
                        fullName,
                        "",
                        avatarUrl
                ));
    }
}
