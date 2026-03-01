package com.teamsolution.lab.service.impl;

import com.teamsolution.lab.dto.request.GoogleLoginRequest;
import com.teamsolution.lab.dto.request.LoginRequest;
import com.teamsolution.lab.dto.request.RefreshRequest;
import com.teamsolution.lab.dto.request.RegisterRequest;
import com.teamsolution.lab.dto.request.SwitchRoleRequest;
import com.teamsolution.lab.dto.request.VerifyEmailRequest;
import com.teamsolution.lab.dto.response.AuthResponse;
import com.teamsolution.lab.dto.response.CustomerProfileGrpcResponse;
import com.teamsolution.lab.dto.response.LoginResponse;
import com.teamsolution.lab.dto.response.ProfileResponse;
import com.teamsolution.lab.dto.response.RegisterResponse;
import com.teamsolution.lab.entity.Account;
import com.teamsolution.lab.entity.RefreshToken;
import com.teamsolution.lab.enums.AccountStatus;
import com.teamsolution.lab.exception.InvalidAccountStatusException;
import com.teamsolution.lab.exception.InvalidTokenException;
import com.teamsolution.lab.grpc.CustomerGrpcClient;
import com.teamsolution.lab.security.CustomUserDetails;
import com.teamsolution.lab.service.AuthService;
import com.teamsolution.lab.service.helper.AccountService;
import com.teamsolution.lab.service.helper.AuthHelperService;
import com.teamsolution.lab.service.helper.JwtTokenService;
import com.teamsolution.lab.service.helper.NotificationService;
import com.teamsolution.lab.service.helper.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final AuthHelperService authHelperService;
    private final RefreshTokenService refreshTokenService;
    private final NotificationService notificationService;
    private final AccountService accountService;
    private final CustomerGrpcClient customerGrpcClient;

    // login
    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        CustomUserDetails userDetails = authenticate(request.email(), request.password());

        Account account = userDetails.account();

        return switch (account.getStatus()) {
            case AccountStatus.PENDING -> authHelperService.handlePendingLogin(account);
            case AccountStatus.ACTIVE -> issueTokensAndSave(request.email(), null);
            default -> throw new InvalidAccountStatusException("Unknown account status: " + account.getStatus());
        };
    }

    // login with Google
    @Override
    @Transactional
    public AuthResponse loginWithGoogle(GoogleLoginRequest request) {
        accountService.findOrCreateGoogleAccount(
                request.email(),
                request.name(),
                request.picture());

        return issueTokensAndSave(request.email(), null);
    }

    // register
    @Override
    public RegisterResponse register(RegisterRequest request) {
        accountService.checkEmailNotExists(request.email());

        String rawOtp = authHelperService.createPendingAccount(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.fullName(),
                request.phone());

        notificationService.sendVerificationEmail(request.email(), rawOtp);

        return new RegisterResponse(
                request.email(),
                15*60
        );
    }

    @Override
    @Transactional
    public void verifyEmail(VerifyEmailRequest request) {
        // find account by email
        Account account = accountService.findByEmail(request.email());

        authHelperService.activatePendingAccount(account.getId(), request.otp());
    }

    // Refresh token
    @Override
    @Transactional
    public AuthResponse refresh(String currentRole, RefreshRequest request) {
        RefreshToken storedToken = refreshTokenService.validateAndGet(request.refreshToken());

        verifyTokenOwnerShip(storedToken, request.refreshToken());

        storedToken.setUsed(true);

        return issueTokensAndSave(storedToken.getAccount().getEmail(), currentRole);
    }

    @Override
    public ProfileResponse getMe(UUID accountId) {
        Account account = accountService.findById(accountId);

        Set<String> roles = accountService.getActiveRoleNamesByAccountId(accountId);

        CustomerProfileGrpcResponse customerProfileResponse =
                customerGrpcClient.getCustomerProfile(account.getId());

        return new ProfileResponse(
                account.getId(),
                account.getEmail(),
                customerProfileResponse.fullName(),
                customerProfileResponse.phone(),
                customerProfileResponse.avatarUrl(),
                account.getStatus(),
                roles
                );
    }

    @Override
    public AuthResponse switchRole(UUID accountId, SwitchRoleRequest request) {
        return issueTokensAndSave(accountService.findById(accountId).getEmail(), request.role());
    }

    private CustomUserDetails authenticate(String email, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        return (CustomUserDetails) auth.getPrincipal();
    }

    private AuthResponse issueTokensAndSave(String email, String selectedRole) {
        Account account = accountService.getAccountWithActiveRoles(email);

        AuthResponse tokens = jwtTokenService.generateTokens(account, selectedRole);
        refreshTokenService.save(account, tokens.refreshToken());
        return tokens;
    }

    private void verifyTokenOwnerShip(RefreshToken storedToken, String rawRefreshToken) {
        UUID accountIdFromJwt = UUID.fromString(
                jwtTokenService.extractSubject(rawRefreshToken)
        );

        if (!storedToken.getAccount().getId().equals(accountIdFromJwt)) {
            throw new InvalidTokenException("Token mismatch");
        }
    }
}
