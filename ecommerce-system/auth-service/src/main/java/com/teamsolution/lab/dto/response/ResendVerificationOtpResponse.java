package com.teamsolution.lab.dto.response;

public record ResendOtpResponse(String email, long verificationExpiresIn) {}
