package com.teamsolution.lab.service.helper;

import com.teamsolution.lab.dto.response.PendingLoginResponse;
import com.teamsolution.lab.entity.Account;
import com.teamsolution.lab.entity.VerificationToken;
import com.teamsolution.lab.enums.AccountRoleStatus;
import com.teamsolution.lab.enums.AccountStatus;
import com.teamsolution.lab.enums.VerificationTokenType;
import com.teamsolution.lab.repository.AccountRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthHelperService {
    private final AccountService accountService;
    private final AccountRoleRepository accountRoleRepository;
    private final NotificationService notificationService;
    private final VerificationTokenService verificationTokenService;

    @Transactional
    public void activatePendingAccount(UUID accountId, String rawOtp) {
        VerificationToken verificationToken =
                verificationTokenService.verifyOtp(
                        accountId,
                        rawOtp,
                        VerificationTokenType.EMAIL_VERIFICATION);

        Account account = verificationToken.getAccount();

        // Activate account
        account.setStatus(AccountStatus.ACTIVE);

        // Activate account role
        accountRoleRepository.activateByAccountId(account.getId());
    }

    public PendingLoginResponse handlePendingLogin(Account account) {

        String rawOtp = verificationTokenService.generateAndSave(account, VerificationTokenType.EMAIL_VERIFICATION);

        notificationService.sendVerificationEmail(account.getEmail(), rawOtp);
        return new PendingLoginResponse(
                account.getEmail(),
                15 * 60); // modify no hard code
    }

    @Transactional
    public String createPendingAccount(String email,
                                       String hashPassword,
                                       String fullName,
                                       String phone) {
        Account savedAccount = accountService.createNewAccount(
                email, hashPassword,
                AccountStatus.PENDING,
                AccountRoleStatus.PENDING,
                fullName,
                phone,
                "");

        return verificationTokenService.generateAndSave(
                savedAccount,
                VerificationTokenType.EMAIL_VERIFICATION);
    }
}
