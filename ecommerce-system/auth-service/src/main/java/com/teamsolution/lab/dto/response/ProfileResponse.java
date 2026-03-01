package com.teamsolution.lab.dto.response;

import com.teamsolution.lab.enums.AccountStatus;

import java.util.Set;
import java.util.UUID;

public record ProfileResponse(
        UUID id,
        String email,
        String phone,
        String fullName,
        String avatarUrl,
        AccountStatus status,
        Set<String> roles
        ) {
}
