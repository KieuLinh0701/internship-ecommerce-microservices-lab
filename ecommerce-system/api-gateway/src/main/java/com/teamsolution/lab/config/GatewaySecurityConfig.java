package com.teamsolution.lab.config;

import com.teamsolution.lab.security.GoogleOAuth2SuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

  @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
  private String jwkSetUri;

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(
      ServerHttpSecurity http, GoogleOAuth2SuccessHandler successHandler) {
    http.csrf(ServerHttpSecurity.CsrfSpec::disable)
        .authorizeExchange(
            exchanges ->
                exchanges
                    // Public endpoints
                    .pathMatchers("/api/auth/**")
                    .permitAll()
                    .pathMatchers("/login/oauth2/code/**")
                    .permitAll()
                    .pathMatchers("/oauth2/**")
                    .permitAll()

                    // All other requests need
                    .anyExchange()
                    .authenticated())
        .oauth2Login(Customizer.withDefaults())
        .oauth2Login(oauth2 -> oauth2.authenticationSuccessHandler(successHandler));

    // CORS configuration for the frontend UI
    //        http.cors(cors -> cors.configurationSource(request -> {
    //            CorsConfiguration config = new CorsConfiguration();
    //            config.setAllowedOrigins(List.of("http://localhost:3000"));
    //            config.setAllowedMethods(List.of("*"));
    //            config.setAllowedHeaders(List.of("*"));
    //            return config;
    //        }));
    return http.build();
  }

  @Bean
  public ReactiveJwtDecoder reactiveJwtDecoder() {
    return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
  }
}
