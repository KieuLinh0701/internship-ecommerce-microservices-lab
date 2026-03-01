package com.teamsolution.lab.config;

import com.teamsolution.lab.config.properties.AuthServiceProperties;
import com.teamsolution.lab.security.GoogleOAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class GatewaySecurityConfig {

  private final AuthServiceProperties authServiceProperties;

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(
      ServerHttpSecurity http, GoogleOAuth2SuccessHandler successHandler) {
    http.csrf(ServerHttpSecurity.CsrfSpec::disable)
        .authorizeExchange(
            exchanges ->
                exchanges
                    // Public endpoints
                        .pathMatchers("/api/auth/**").permitAll()
                        .pathMatchers("/login/oauth2/code/**").permitAll()
                        .pathMatchers("/oauth2/**").permitAll()
                        .pathMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/webjars/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs"
                        ).permitAll()

                      // All other requests need
                      .anyExchange().authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
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
  public ReactiveJwtDecoder reactiveJwtDecoder(@LoadBalanced WebClient.Builder webClientBuilder) {
    WebClient webClient = webClientBuilder.build();

    return NimbusReactiveJwtDecoder
            .withJwkSetUri(authServiceProperties.getJwksUri())
            .webClient(webClient)
            .build();
  }
}
