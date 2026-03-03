package com.teamsolution.lab.service.helper;

import com.teamsolution.lab.dto.response.PendingLoginResponse;
import com.teamsolution.lab.dto.response.SendOtpResetPasswordResponse;
import com.teamsolution.lab.entity.Account;
import com.teamsolution.lab.entity.VerificationToken;
import com.teamsolution.lab.enums.AccountRoleStatus;
import com.teamsolution.lab.enums.AccountStatus;
import com.teamsolution.lab.enums.VerificationTokenType;
import com.teamsolution.lab.exception.InvalidTokenException;
import com.teamsolution.lab.repository.AccountRoleRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthHelperService {
  private final AccountService accountService;
  private final AccountRoleRepository accountRoleRepository;
  private final NotificationService notificationService;
  private final VerificationTokenService verificationTokenService;
  private final JwtTokenService jwtTokenService;

  @Transactional
  public void activatePendingAccount(UUID accountId, String rawOtp) {
    VerificationToken verificationToken =
        verificationTokenService.verifyOtp(
            accountId, rawOtp, VerificationTokenType.EMAIL_VERIFICATION);

    Account account = verificationToken.getAccount();

    // Activate account
    account.setStatus(AccountStatus.ACTIVE);

    // Activate account role
    accountRoleRepository.activateByAccountId(account.getId());
  }

  public PendingLoginResponse handlePendingLogin(Account account) {

    String rawOtp =
        verificationTokenService.generateAndSave(account, VerificationTokenType.EMAIL_VERIFICATION);

    notificationService.sendVerificationEmail(account.getEmail(), rawOtp);
    return new PendingLoginResponse(account.getEmail(), 15 * 60); // modify no hard code
  }

  @Transactional
  public String createPendingAccount(
      String email, String hashPassword, String fullName, String phone) {
    Account savedAccount =
        accountService.createNewAccount(
            email,
            hashPassword,
            AccountStatus.PENDING,
            AccountRoleStatus.PENDING,
            fullName,
            phone,
            "");

    return verificationTokenService.generateAndSave(
        savedAccount, VerificationTokenType.EMAIL_VERIFICATION);
  }

  public SendOtpResetPasswordResponse handleResetPassword(Account account) {

    String rawOtp =
        verificationTokenService.generateAndSave(account, VerificationTokenType.PASSWORD_RESET);

    // Temporary use send verification email to test
    notificationService.sendVerificationEmail(account.getEmail(), rawOtp);
    return new SendOtpResetPasswordResponse(account.getEmail(), 15 * 60); // modify no hard code
  }

  @Transactional
  public String verifyOtpAndGenerateResetToken(UUID accountId, String rawOtp) {
    VerificationToken verificationToken =
        verificationTokenService.verifyOtp(accountId, rawOtp, VerificationTokenType.PASSWORD_RESET);

    return jwtTokenService.generateToken(
        verificationToken.getAccount(), VerificationTokenType.PASSWORD_RESET.name(), 5);
  }

  public String validateToken(String token, String type) {
    String email = jwtTokenService.extractEmail(token);

    if (email == null || jwtTokenService.isTokenExpired(token)) {
      throw new InvalidTokenException("Reset token expired");
    }

    String tokenType = jwtTokenService.extractTokenType(token);
    if (!type.equals(tokenType)) {
      throw new InvalidTokenException("Invalid token type");
    }

    return email;
  }
}
