package com.teamsolution.lab.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            title = "Auth Service API",
            version = "v1.0",
            description = "Authentication and authorization"),
    servers = {
            @Server(url = "http://localhost:9000/api/auth", description = "Gateway - Local")
    })
public class OpenApiConfig {}
