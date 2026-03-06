package com.teamsolution.lab.dto.response;

public record ResendVerificationOtpResponse(String email, long verificationExpiresIn) {}
