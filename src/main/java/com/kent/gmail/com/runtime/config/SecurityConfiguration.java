package com.kent.gmail.com.runtime.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.kent.gmail.com.runtime.security.JwtTokenFilter;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

  @Value("${api.prefix:/api}")
  private String apiPrefix;

  @Value("${oauth2.client.redirect-uri:http://localhost:8080/auth?}")
  private String clientRedirectUri;

  private final UserDetailsService userDetailsService;
  private final JwtTokenFilter jwtTokenFilter;

  public SecurityConfiguration(
      UserDetailsService userDetailsService, JwtTokenFilter jwtTokenFilter) {
    this.userDetailsService = userDetailsService;
    this.jwtTokenFilter = jwtTokenFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // Enable CORS and disable CSRF
    http.cors(withDefaults())
        .csrf(f -> f.disable())
        // Set session management to stateless
        .sessionManagement(f -> f.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Set unauthorized requests exception handler
        .exceptionHandling(
            f ->
                f.authenticationEntryPoint(
                    (request, response, ex) -> {
                      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                    }))
        .authorizeHttpRequests(
            f ->
                f
                    // Set permissions on endpoints
                    .requestMatchers(
                        apiPrefix + "/login",
                        apiPrefix + "/register",
                        apiPrefix + "/v3/api-docs/**")
                    .permitAll()
                    .requestMatchers(apiPrefix + "/**")
                    .authenticated()
                    .anyRequest()
                    .permitAll());

    http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder =
        http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.userDetailsService(userDetailsService);
    return authenticationManagerBuilder.build();
  }

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(
            new Components()
                .addSecuritySchemes(
                    "bearer-jwt",
                    new io.swagger.v3.oas.models.security.SecurityScheme()
                        .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER)
                        .name("Authorization")))
        .addSecurityItem(
            new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write")));
  }
}
