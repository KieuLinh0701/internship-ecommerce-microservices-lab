package com.teamsolution.lab.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SwitchRoleRequest(
        @NotBlank(message = "Role is required")
        String role
) {
}
