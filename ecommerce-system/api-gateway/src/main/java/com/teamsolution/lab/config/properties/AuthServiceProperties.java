package com.teamsolution.lab.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.auth-service")
@Component
@Getter
@Setter
public class AuthServiceProperties {
  private String googleLoginUri;
}
