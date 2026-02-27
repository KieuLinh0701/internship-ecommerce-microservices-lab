package com.teamsolution.lab.service.helper;

import com.teamsolution.lab.entity.Account;
import com.teamsolution.lab.entity.AccountRole;
import com.teamsolution.lab.entity.AccountRoleId;
import com.teamsolution.lab.entity.Role;
import com.teamsolution.lab.entity.VerificationToken;
import com.teamsolution.lab.enums.AccountRoleStatus;
import com.teamsolution.lab.enums.AccountStatus;
import com.teamsolution.lab.enums.RoleStatus;
import com.teamsolution.lab.enums.VerificationTokenType;
import com.teamsolution.lab.exception.ResourceNotFoundException;
import com.teamsolution.lab.repository.AccountRepository;
import com.teamsolution.lab.repository.AccountRoleRepository;
import com.teamsolution.lab.repository.RoleRepository;
import com.teamsolution.lab.repository.VerificationTokenRepository;
import com.teamsolution.lab.util.OtpUtils;
import com.teamsolution.lab.util.UuidGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthHelperService {
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    @Transactional
    public String createPendingAccount(String email, String hashPassword) {
        Account savedAccount = createNewAccount(
                email, hashPassword,
                AccountStatus.PENDING, false,
                AccountRoleStatus.PENDING);

        return resendOtp(savedAccount.getId(), savedAccount);
    }

    public String resendOtp(UUID accountId, Account account) {
        verificationTokenRepository.markOldOtpUsed(accountId, VerificationTokenType.EMAIL_VERIFICATION);

        String rawOtp = RandomStringUtils.randomNumeric(6);

        String hashedOtp = OtpUtils.hashOtp(rawOtp);

        VerificationToken token =
                VerificationToken.builder()
                        .id(UuidGenerator.generate())
                        .account(account)
                        .token(hashedOtp)
                        .type(VerificationTokenType.EMAIL_VERIFICATION)
                        .expiresAt(LocalDateTime.now().plusMinutes(15))
                        .isUsed(false)
                        .build();

        verificationTokenRepository.save(token);

        return rawOtp;
    }

    @Transactional
    public Account createGoogleAccount(String email) {
        return createNewAccount(email, "",
                AccountStatus.ACTIVE, true,
                AccountRoleStatus.ACTIVE);
    }

    private Account createNewAccount(String email, String hashPassword,
                                     AccountStatus accountStatus, boolean isVerified,
                                     AccountRoleStatus accountRoleStatus) {
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

        // Create customer in here

        return saved;
    }

    @Transactional
    public void activatePendingAccount(UUID accountId, String rawOtp) {
        // Hash the incoming OTP to compare with stored hashed OTP
        String hashedOtp = OtpUtils.hashOtp(rawOtp);

        // find token by account id, token, type and isUsed false
        VerificationToken verificationToken =
                verificationTokenRepository
                        .findByAccountIdAndTokenAndTypeAndIsUsedFalse(
                                accountId, hashedOtp, VerificationTokenType.EMAIL_VERIFICATION)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Invalid or used token"));

        // check if token expired
        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired"); // modify later to custom exception
        }

        // Mark token used
        verificationToken.setUsed(true);

        Account account = verificationToken.getAccount();

        // Activate account
        account.setStatus(AccountStatus.ACTIVE);

        // Activate account role
        accountRoleRepository.activateByAccountId(account.getId());
    }

}
