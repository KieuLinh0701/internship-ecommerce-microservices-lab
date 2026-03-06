package com.teamsolution.lab.controller;

import com.teamsolution.lab.dto.request.ChangePasswordRequest;
import com.teamsolution.lab.dto.request.GoogleLoginRequest;
import com.teamsolution.lab.dto.request.LoginRequest;
import com.teamsolution.lab.dto.request.RefreshRequest;
import com.teamsolution.lab.dto.request.RegisterRequest;
import com.teamsolution.lab.dto.request.ResendOtpPasswordResetRequest;
import com.teamsolution.lab.dto.request.ResendVerificationOtpRequest;
import com.teamsolution.lab.dto.request.ResetPasswordRequest;
import com.teamsolution.lab.dto.request.SendOtpResetPasswordRequest;
import com.teamsolution.lab.dto.request.SwitchRoleRequest;
import com.teamsolution.lab.dto.request.VerifyEmailRequest;
import com.teamsolution.lab.dto.request.VerifyPasswordResetRequest;
import com.teamsolution.lab.dto.response.AuthResponse;
import com.teamsolution.lab.dto.response.LoginResponse;
import com.teamsolution.lab.dto.response.ProfileResponse;
import com.teamsolution.lab.dto.response.RegisterResponse;
import com.teamsolution.lab.dto.response.ResendOtpPasswordResetResponse;
import com.teamsolution.lab.dto.response.ResendVerificationOtpResponse;
import com.teamsolution.lab.dto.response.SendOtpResetPasswordResponse;
import com.teamsolution.lab.dto.response.VerifyPasswordResetResponse;
import com.teamsolution.lab.exception.SameRoleException;
import com.teamsolution.lab.response.ApiResponse;
import com.teamsolution.lab.security.SecurityUtils;
import com.teamsolution.lab.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResponse>> login(
      @Valid @RequestBody LoginRequest request) {

      LoginResponse response = authService.login(request);
      return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping("/oauth2/google")
  public ResponseEntity<ApiResponse<AuthResponse>> loginWithGoogle(
      @Valid @RequestBody GoogleLoginRequest request) {

      AuthResponse response = authService.loginWithGoogle(request);
      return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<RegisterResponse>> register(
      @Valid @RequestBody RegisterRequest request) {

      RegisterResponse response = authService.register(request);
      return ResponseEntity.ok(
        ApiResponse.success(
            "Registration was successful. Please check your email to verify your account.",
            response));
  }

  @PostMapping("/verify-email")
  public ResponseEntity<ApiResponse<Void>> verifyEmail(
            @Valid @RequestBody VerifyEmailRequest request) {

      authService.verifyEmail(request);
      return ResponseEntity.ok(ApiResponse.success(null));
  }

  @PostMapping("/resend-verification-otp")
  public ResponseEntity<ApiResponse<ResendVerificationOtpResponse>> resendVerificationOtp(
            @Valid @RequestBody ResendVerificationOtpRequest request) {

      ResendVerificationOtpResponse response = authService.resendVerificationOtp(request);
      return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping("/refresh")
  public ResponseEntity<ApiResponse<AuthResponse>> refresh(
          @RequestBody RefreshRequest request) {

      String currentRole = SecurityUtils.getCurrentRole();

      AuthResponse response = authService.refresh(currentRole, request);
      return ResponseEntity.ok(ApiResponse.success(response));
  }

  @GetMapping("/me")
  public ResponseEntity<ApiResponse<ProfileResponse>> getMe() {

      UUID accountId = SecurityUtils.getCurrentAccountId();

      ProfileResponse response = authService.getMe(accountId);
      return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping("/switch-role")
  public ResponseEntity<ApiResponse<AuthResponse>> switchRole(
      @Valid @RequestBody SwitchRoleRequest request) {

      UUID accountId = SecurityUtils.getCurrentAccountId();
      String currentRole = SecurityUtils.getCurrentRole();

      if (currentRole.equals(request.role())) {
          throw new SameRoleException("Already using this role");
      }

      AuthResponse response = authService.switchRole(accountId, request);
      return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping("/password-reset/send-otp")
  public ResponseEntity<ApiResponse<SendOtpResetPasswordResponse>> sendOtpResetPassword(
      @Valid @RequestBody SendOtpResetPasswordRequest request) {

      SendOtpResetPasswordResponse response = authService.sendOtpResetPassword(request);
      return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping("/password-reset/resend-otp")
  public ResponseEntity<ApiResponse<ResendOtpPasswordResetResponse>> resendOtpResetPassword(
          @Valid @RequestBody ResendOtpPasswordResetRequest request) {

      ResendOtpPasswordResetResponse response = authService.resendOtpPasswordReset(request);
      return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping("/password-reset/verify")
  public ResponseEntity<ApiResponse<VerifyPasswordResetResponse>> verifyPasswordReset(
      @Valid @RequestBody VerifyPasswordResetRequest request) {

    VerifyPasswordResetResponse response = authService.verifyResetPassword(request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping("/password-reset/reset")
  public ResponseEntity<ApiResponse<Void>> resetPassword(
      @Valid @RequestBody ResetPasswordRequest request) {

    authService.resetPassword(request);
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  @PutMapping("/change-password")
  public ResponseEntity<ApiResponse<Void>> changePassword(
          @Valid @RequestBody ChangePasswordRequest request) {

      UUID accountId = SecurityUtils.getCurrentAccountId();

      authService.changePassword(accountId, request);
      return ResponseEntity.ok(ApiResponse.success(null));
  }
}
