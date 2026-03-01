package com.teamsolution.lab.controller;

import com.teamsolution.lab.dto.request.GoogleLoginRequest;
import com.teamsolution.lab.dto.request.LoginRequest;
import com.teamsolution.lab.dto.request.RefreshRequest;
import com.teamsolution.lab.dto.request.RegisterRequest;
import com.teamsolution.lab.dto.request.SwitchRoleRequest;
import com.teamsolution.lab.dto.request.VerifyEmailRequest;
import com.teamsolution.lab.dto.response.AuthResponse;
import com.teamsolution.lab.dto.response.LoginResponse;
import com.teamsolution.lab.dto.response.ProfileResponse;
import com.teamsolution.lab.dto.response.RegisterResponse;
import com.teamsolution.lab.exception.SameRoleException;
import com.teamsolution.lab.response.ApiResponse;
import com.teamsolution.lab.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  // Login
  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.ok(ApiResponse.success(authService.login(request)));
  }

  // Login with Google
  @PostMapping("/oauth2/google")
  public ResponseEntity<ApiResponse<AuthResponse>> loginWithGoogle(
      @Valid @RequestBody GoogleLoginRequest request) {
    return ResponseEntity.ok(ApiResponse.success(authService.loginWithGoogle(request)));
  }

  // Register
  @PostMapping("/register")
  public ResponseEntity<ApiResponse<RegisterResponse>> register(
      @Valid @RequestBody RegisterRequest request) {
    return ResponseEntity.ok(ApiResponse.success("Registration was successful. Please check your email to verify your account.", authService.register(request)));
  }

  @PostMapping("/verify-email")
  public ResponseEntity<ApiResponse<Void>> verifyEmail(
      @Valid @RequestBody VerifyEmailRequest request) {
    authService.verifyEmail(request);
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  // Refresh token
  @PostMapping("/refresh")
  public ResponseEntity<ApiResponse<AuthResponse>> refresh(
          @RequestHeader("X-Role") String currentRole,
          @RequestBody RefreshRequest request) {
    return ResponseEntity.ok(ApiResponse.success(authService.refresh(currentRole, request)));
  }

  @GetMapping("/me")
  public ResponseEntity<ApiResponse<ProfileResponse>> getMe(
          @RequestHeader("X-Account-Id") String accountId) {

    ProfileResponse profileResponse = authService.getMe(UUID.fromString(accountId));
    return ResponseEntity.ok(ApiResponse.success(profileResponse));
  }

  @PostMapping("/switch-role")
  public ResponseEntity<ApiResponse<AuthResponse>> switchRole(
          @RequestHeader("X-Account-Id") String accountId,
          @RequestHeader("X-Role") String currentRole,
          @Valid @RequestBody SwitchRoleRequest request) {

    if (currentRole.equals(request.role())) {
      throw new SameRoleException("Already using this role");
    }

    AuthResponse response = authService.switchRole(UUID.fromString(accountId), request);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

}
