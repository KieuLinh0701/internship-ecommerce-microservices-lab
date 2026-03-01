package com.teamsolution.lab.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            title = "Customer Service API",
            version = "v1.0"),
    servers = {
            @Server(url = "http://localhost:9000/api/customers", description = "Gateway - Local")
    })
public class OpenApiConfig {}
