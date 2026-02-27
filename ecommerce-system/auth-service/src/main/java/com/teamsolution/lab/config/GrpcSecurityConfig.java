package com.teamsolution.lab.config;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcSecurityConfig {

  @Bean
  public GrpcAuthenticationReader grpcAuthenticationReader() {
    return (ServerCall<?, ?> call, Metadata headers) -> {
      return null;
    };
  }
}
