package com.teamsolution.lab.dto.response;

public record ResendOtpPasswordResetResponse(String email, long verificationExpiresIn) {}
