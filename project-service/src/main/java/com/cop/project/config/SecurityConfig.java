package com.cop.project.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import com.cop.common.security.AbstractSecurityConfig;

@Configuration
@EnableMethodSecurity
@Profile("!test")
public class SecurityConfig extends AbstractSecurityConfig {

    protected SecurityConfig(@Value("${keycloak.realm-url}") String realmUrl,
            @Value("${keycloak.client-id}") String clientId, @Value("${keycloak.client-secret}") String clientSecret,
            @Value("${keycloak.resource-set}") String[] resourceSet) {
        super(realmUrl, clientId, clientSecret, resourceSet);
    }

    protected void configureAuthorization(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/**").permitAll()
                .requestMatchers("/api/v2/project/search").hasAnyRole("admin", "editor")
                .requestMatchers("/api/v2/project/search").hasAuthority("SCOPE_read")
                .requestMatchers("/actuator/**", "/swagger-ui/**", "/api-docs/**", "/project/api-docs/**").permitAll()
                .requestMatchers(new RegexRequestMatcher(".*/api-docs/.*", null)).permitAll()
                .requestMatchers(antMatcher("/h2-console/**")).permitAll().anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())
                        .jwtAuthenticationConverter(keycloakJwtAuthenticationConverter())));
    }

}
