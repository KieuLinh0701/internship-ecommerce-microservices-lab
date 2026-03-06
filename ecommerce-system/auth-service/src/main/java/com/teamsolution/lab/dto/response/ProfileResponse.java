package com.teamsolution.lab.dto.response;

import com.teamsolution.lab.enums.AccountStatus;
import java.util.Set;
import java.util.UUID;

public record ProfileResponse(
    UUID id,
    String email,
    AccountStatus status,
    Set<String> roles) {}
