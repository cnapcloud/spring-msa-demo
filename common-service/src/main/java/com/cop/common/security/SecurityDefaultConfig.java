package com.cop.common.security;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.extern.slf4j.Slf4j;

/**
 * When {@code spring-security-oauth2-resource-server} is included, Spring
 * Security may automatically enforce authentication.
 * 
 * This configuration permits all HTTP requests regardless of authentication.
 * However, if a JWT token is present, a {@link GrantedAuthority} will be
 * created and set into the {@link SecurityContextHolder}.
 * 
 * Note: This is not suitable for production use, as it relies on an unsafe JWT
 * decoder.
 */
@ConditionalOnMissingBean(AbstractSecurityConfig.class)
@Configuration
@Slf4j
public class SecurityDefaultConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }

    private UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public JwtAuthenticationConverter keycloakJwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakAuthorizationConverter());

        return converter;
    }
}
