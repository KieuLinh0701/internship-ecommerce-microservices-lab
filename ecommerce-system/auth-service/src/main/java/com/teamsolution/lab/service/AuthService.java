package com.teamsolution.lab.service;

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

import java.util.UUID;

public interface AuthService {

  // login
  public LoginResponse login(LoginRequest request);

  // login with Google
  public AuthResponse loginWithGoogle(GoogleLoginRequest request);

  // register
  public RegisterResponse register(RegisterRequest request);

  // verify email
  public void verifyEmail(VerifyEmailRequest request);

  // refresh token
  public AuthResponse refresh(String currentRole, RefreshRequest request);

  // get profile
  public ProfileResponse getMe(UUID accountId);

  // switch role
  public AuthResponse switchRole(UUID accountId, SwitchRoleRequest request);
}
