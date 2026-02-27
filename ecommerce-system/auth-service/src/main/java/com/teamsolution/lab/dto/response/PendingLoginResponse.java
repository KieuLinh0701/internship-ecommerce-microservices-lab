package com.teamsolution.lab.dto.response;

public record PendingLoginResponse(String email, long verificationExpiresIn) implements LoginResponse {}
