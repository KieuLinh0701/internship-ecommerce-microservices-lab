package com.teamsolution.lab.dto.response;

public record SendOtpResetPasswordResponse(String email, long verificationExpiresIn) {}
