package com.teamsolution.lab.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMQProperties {
  private String queue;
  private String exchange;
  private String routingKey;
}