package com.teamsolution.lab.service.impl;

import com.teamsolution.lab.config.properties.JwtProperties;
import com.teamsolution.lab.dto.request.GoogleLoginRequest;
import com.teamsolution.lab.dto.request.LoginRequest;
import com.teamsolution.lab.dto.request.RefreshRequest;
import com.teamsolution.lab.dto.request.RegisterRequest;
import com.teamsolution.lab.dto.request.VerifyEmailRequest;
import com.teamsolution.lab.dto.response.AuthResponse;
import com.teamsolution.lab.dto.response.EmailResponse;
import com.teamsolution.lab.dto.response.LoginResponse;
import com.teamsolution.lab.dto.response.PendingLoginResponse;
import com.teamsolution.lab.dto.response.RegisterResponse;
import com.teamsolution.lab.entity.Account;
import com.teamsolution.lab.entity.RefreshToken;
import com.teamsolution.lab.enums.AccountRoleStatus;
import com.teamsolution.lab.enums.AccountStatus;
import com.teamsolution.lab.enums.RoleStatus;
import com.teamsolution.lab.exception.DuplicateResourceException;
import com.teamsolution.lab.exception.ResourceNotFoundException;
import com.teamsolution.lab.grpc.EmailGrpcClient;
import com.teamsolution.lab.repository.AccountRepository;
import com.teamsolution.lab.repository.RefreshTokenRepository;
import com.teamsolution.lab.security.CustomUserDetails;
import com.teamsolution.lab.service.AuthService;
import com.teamsolution.lab.service.JwtTokenService;
import com.teamsolution.lab.service.helper.AuthHelperService;
import com.teamsolution.lab.util.OtpUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final EmailGrpcClient emailGrpcClient;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;
    private final AuthHelperService authHelperService;

    // login
    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        Authentication auth =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        Account account = userDetails.account();

        if (account.getIsDelete()) {
            throw new RuntimeException("Account is deleted");
        }

        return switch (account.getStatus()) {
            case AccountStatus.PENDING -> loginPendingAccount(account);
            case AccountStatus.ACTIVE -> loginValidAccount(request.email());
            case INACTIVE -> throw new RuntimeException("Account is inactive");
            case SUSPENDED -> throw new RuntimeException("Account is suspended");
            default -> throw new RuntimeException("Unknown account status: " + account.getStatus());
        };
    }

    // login with Google
    @Override
    @Transactional
    public AuthResponse loginWithGoogle(GoogleLoginRequest request) {
        accountRepository
                .findByEmail(request.email())
                .orElseGet(
                        () -> authHelperService.createGoogleAccount(request.email())
                );

        Account account = accountRepository.findByEmailWithActiveRoles(
                    request.email(),
                    AccountStatus.ACTIVE,
                    AccountRoleStatus.ACTIVE,
                    RoleStatus.ACTIVE
                )
                .orElseThrow();

        AuthResponse tokens = jwtTokenService.generateTokens(account);
        saveRefreshToken(account, tokens.refreshToken());

        return tokens;
    }

    // register
    @Override
    public RegisterResponse register(RegisterRequest request) {
        // Check if email already exists
        if (accountRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already exists");
        }

        String rawOtp = authHelperService.createPendingAccount(
                request.email(),
                passwordEncoder.encode(request.password()));

        return sendVerificationEmail(request.email(), rawOtp);
    }

    @Override
    @Transactional
    public void verifyEmail(VerifyEmailRequest request) {

        // find account by email
        Account account =
                accountRepository
                        .findByEmail(request.email())
                        .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        authHelperService.activatePendingAccount(account.getId(), request.otp());
    }

    // Refresh token
    @Override
    @Transactional
    public AuthResponse refresh(RefreshRequest request) {
        String hashedToken = OtpUtils.hashOtp(request.refreshToken());

        RefreshToken storedToken = refreshTokenRepository
                .findByTokenAndIsUsedFalse(hashedToken)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or used refresh token"));

        if (storedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired"); // modify later to custom exception
        }

        UUID accountIdFromJwt = UUID.fromString(
                jwtTokenService.extractSubject(request.refreshToken())
        );

        if (!storedToken.getAccount().getId().equals(accountIdFromJwt)) {
            throw new RuntimeException("Token mismatch"); // modify later to custom exception
        }

        storedToken.setUsed(true);

        Account account = accountRepository.findByEmailWithActiveRoles(
                storedToken.getAccount().getEmail(),
                AccountStatus.ACTIVE,
                AccountRoleStatus.ACTIVE,
                RoleStatus.ACTIVE
        ).orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        AuthResponse tokens = jwtTokenService.generateTokens(account);
        saveRefreshToken(account, tokens.refreshToken());
        return tokens;
    }

    private void saveRefreshToken(Account account, String rawRefreshToken) {
        String hashedToken = OtpUtils.hashOtp(rawRefreshToken);
        RefreshToken refreshToken = RefreshToken.builder()
                .account(account)
                .token(hashedToken)
                .isUsed(false)
                .expiresAt(LocalDateTime.now().plusSeconds(jwtProperties.getRefreshTokenExpiration()))
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    private PendingLoginResponse loginPendingAccount(Account account) {

        String rawOtp = authHelperService.resendOtp(account.getId(), account);

        sendVerificationEmail(account.getEmail(), rawOtp);
        return new PendingLoginResponse(account.getEmail(), 15 * 60);
    }

    private AuthResponse loginValidAccount(String email) {
        Account accountWithRoles = accountRepository.findByEmailWithActiveRoles(
                        email,
                        AccountStatus.ACTIVE,
                        AccountRoleStatus.ACTIVE,
                        RoleStatus.ACTIVE
                )
                .orElseThrow();

        AuthResponse tokens = jwtTokenService.generateTokens(accountWithRoles);
        saveRefreshToken(accountWithRoles, tokens.refreshToken());

        return tokens;
    }

    // Send verification email for registration or unverified login
    private RegisterResponse sendVerificationEmail(String email, String rawOtp) {
        EmailResponse response = emailGrpcClient.sendVerificationEmail(
                email,
                "Thank you for registering with us!",
                "Your verification otp: " + rawOtp);
        return response.success()
                ? new RegisterResponse(email, 15 * 60)
                : new RegisterResponse(null, 0);
    }
}
