package com.teamsolution.lab.dto.response;

public record AuthResponse(String accessToken, String refreshToken) implements LoginResponse {}
