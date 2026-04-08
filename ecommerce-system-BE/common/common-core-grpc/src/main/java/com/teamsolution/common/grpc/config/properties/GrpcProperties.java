package com.teamsolution.common.grpc.config.properties;

import jakarta.validation.constraints.Min;
import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "grpc")
public class GrpcProperties {

  private Retry retry;
  private Duration timeout;

  @Data
  public static class Retry {

    @Min(1)
    private int maxAttempts;

    private long baseDelayMs;
  }
}
