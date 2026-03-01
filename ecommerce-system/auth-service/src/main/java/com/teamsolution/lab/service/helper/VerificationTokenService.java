package com.teamsolution.lab.service.helper;

import com.teamsolution.lab.entity.Account;
import com.teamsolution.lab.entity.VerificationToken;
import com.teamsolution.lab.enums.VerificationTokenType;
import com.teamsolution.lab.exception.InvalidTokenException;
import com.teamsolution.lab.exception.ResourceNotFoundException;
import com.teamsolution.lab.repository.VerificationTokenRepository;
import com.teamsolution.lab.util.OtpUtils;
import com.teamsolution.lab.util.UuidGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;

    public String generateAndSave(Account account, VerificationTokenType type) {

        verificationTokenRepository.markOldOtpUsed(account.getId(), type);

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

    public VerificationToken verifyOtp(UUID accountId, String rawOtp, VerificationTokenType type) {
        // Hash the incoming OTP to compare with stored hashed OTP
        String hashedOtp = OtpUtils.hashOtp(rawOtp);

        // find token by account id, token, type and isUsed false
        VerificationToken verificationToken =
                verificationTokenRepository
                        .findByAccountIdAndTokenAndTypeAndIsUsedFalse(
                                accountId, hashedOtp, type)
                        .orElseThrow(() -> new ResourceNotFoundException("Invalid or used token"));

        // check if token expired
        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Token expired");
        }

        // Mark token used
        verificationToken.setUsed(true);

        // Save
        verificationTokenRepository.save(verificationToken);

        return verificationToken;
    }
}
