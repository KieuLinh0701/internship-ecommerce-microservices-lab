package com.teamsolution.lab.service.helper;

import com.teamsolution.lab.config.properties.OtpSecurityProperties;
import com.teamsolution.lab.dto.response.PendingLoginResponse;
import com.teamsolution.lab.dto.response.SendOtpResetPasswordResponse;
import com.teamsolution.lab.entity.Account;
import com.teamsolution.lab.entity.VerificationToken;
import com.teamsolution.lab.enums.AccountRoleStatus;
import com.teamsolution.lab.enums.AccountStatus;
import com.teamsolution.lab.enums.VerificationTokenType;
import com.teamsolution.lab.exception.InvalidTokenException;
import com.teamsolution.lab.exception.MaxResendOtpExceededException;
import com.teamsolution.lab.exception.OtpCooldownException;
import com.teamsolution.lab.kafka.NotificationEventProducer;
import com.teamsolution.lab.kafka.enums.NotificationEventType;
import com.teamsolution.lab.repository.AccountRoleRepository;
import com.teamsolution.lab.util.OtpUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthHelperService {
  private final AccountService accountService;
  private final AccountRoleRepository accountRoleRepository;
  private final NotificationEventProducer notificationEventProducer;
  private final VerificationTokenService verificationTokenService;
  private final JwtTokenService jwtTokenService;
  private final OtpSecurityProperties otpSecurityProperties;

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

    notificationEventProducer.sendOtpEmail(account.getEmail(), rawOtp, NotificationEventType.EMAIL_VERIFICATION);
    return new PendingLoginResponse(account.getEmail(), otpSecurityProperties.getVerificationExpiresInSeconds());
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
    notificationEventProducer.sendOtpEmail(account.getEmail(), rawOtp, NotificationEventType.PASSWORD_RESET);
    return new SendOtpResetPasswordResponse(account.getEmail(), otpSecurityProperties.getVerificationExpiresInSeconds());
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

    public void handleResendOtp(Account account,
            VerificationTokenType type) {

        VerificationToken token = verificationTokenService
                .findActiveToken(account.getId(), type);

        LocalDateTime now = LocalDateTime.now();

        String rawOtp;

        if (token.getExpiresAt().isBefore(now)) {
            rawOtp = verificationTokenService.generateAndSave(account, type);
            notificationEventProducer.sendOtpEmail(account.getEmail(), rawOtp, toNotificationEventType(type));
            return;
        }

        if (token.getResendCount() >= otpSecurityProperties.getMaxResend()) {
            throw new MaxResendOtpExceededException("You have reached the maximum number of resend attempts. Please wait until your verification code expires before requesting a new one");
        }

        if (Duration.between(token.getLastSentAt(), now)
                .getSeconds() < otpSecurityProperties.getCooldownSeconds()) {
            throw new OtpCooldownException("Please wait before resending");
        }

        rawOtp = RandomStringUtils.randomNumeric(6);
        token.setToken(OtpUtils.hashOtp(rawOtp));
        token.setResendCount(token.getResendCount() + 1);
        token.setLastSentAt(now);

        verificationTokenService.save(token);

        notificationEventProducer.sendOtpEmail(account.getEmail(), rawOtp, toNotificationEventType(type));
    }

    private NotificationEventType toNotificationEventType(VerificationTokenType type) {
        return switch (type) {
            case EMAIL_VERIFICATION -> NotificationEventType.EMAIL_VERIFICATION;
            case PASSWORD_RESET -> NotificationEventType.PASSWORD_RESET;
            case PASSWORD_RESET_TOKEN -> null;
        };
    }
}
