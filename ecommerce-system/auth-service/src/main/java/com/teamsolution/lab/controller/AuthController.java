package com.teamsolution.lab.controller;

import com.teamsolution.lab.dto.request.GoogleLoginRequest;
import com.teamsolution.lab.dto.request.LoginRequest;
import com.teamsolution.lab.dto.request.RefreshRequest;
import com.teamsolution.lab.dto.request.RegisterRequest;
import com.teamsolution.lab.dto.request.VerifyEmailRequest;
import com.teamsolution.lab.dto.response.AuthResponse;
import com.teamsolution.lab.dto.response.LoginResponse;
import com.teamsolution.lab.dto.response.RegisterResponse;
import com.teamsolution.lab.response.ApiResponse;
import com.teamsolution.lab.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    return ResponseEntity.ok(ApiResponse.success(authService.register(request)));
  }

  @PostMapping("/verify-email")
  public ResponseEntity<ApiResponse<Void>> verifyEmail(
      @Valid @RequestBody VerifyEmailRequest request) {
    authService.verifyEmail(request);
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  // Refresh token
  @PostMapping("/refresh")
  public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshRequest request) {
    return ResponseEntity.ok(ApiResponse.success(authService.refresh(request)));
  }
}
