package com.teamsolution.lab.dto.response;

public sealed interface LoginResponse permits AuthResponse, PendingLoginResponse {
}
