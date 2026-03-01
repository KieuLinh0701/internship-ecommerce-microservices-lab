package com.teamsolution.lab.dto.response;

public record CustomerProfileGrpcResponse(
        String fullName,
        String phone,
        String avatarUrl) {}
