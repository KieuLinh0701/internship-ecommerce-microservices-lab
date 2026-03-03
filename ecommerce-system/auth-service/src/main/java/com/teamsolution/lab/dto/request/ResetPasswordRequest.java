package com.teamsolution.lab.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
    @NotBlank(message = "Token is required") String tokenReset,
    @NotBlank(message = "Password is required") String password) {}
