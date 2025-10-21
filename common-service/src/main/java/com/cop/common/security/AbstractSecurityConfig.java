package com.cop.common.security;

import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractSecurityConfig {
    private final String realmUrl;
    private final String clientId;
    private final String clientSecret;
    private final String[] resourceSet;

    protected AbstractSecurityConfig(String realmUrl, String clientId, String clientSecret, String[] resourceSet) {
        this.realmUrl = realmUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.resourceSet = resourceSet;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        applyCommonSettings(http);
        configureAuthorization(http);

        return http.build();
    }

    protected void applyCommonSettings(HttpSecurity http) throws Exception {
        http.addFilterBefore(new RequestContextFilter(), BearerTokenAuthenticationFilter.class);
        http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedEntryPoint()));
    }

    // This method should be implemented by subclasses to define specific
    // authorization rules.
    protected abstract void configureAuthorization(HttpSecurity http) throws Exception;

    // It verifies the signature, ignores the issuer, and only validates basic
    // time-related claims.
    @Bean
    public JwtDecoder jwtDecoder() {
        return new JwtDecoder() {
            KeycloakPublicKeyProvider provider = new KeycloakPublicKeyProvider(realmUrl);

            @Override
            public Jwt decode(String token) throws JwtException {
                try {
                    SignedJWT signedJWT = SignedJWT.parse(token);
                    String kid = signedJWT.getHeader().getKeyID();

                    RSAPublicKey key = provider.getKeyByKid(kid);
                    JWSVerifier verifier = new RSASSAVerifier(key);

                    if (!signedJWT.verify(verifier)) {
                        throw new BadJwtException("Invalid signature");
                    }

                    // Claims parsing
                    JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
                    Map<String, Object> claims = claimsSet.getClaims();
                    Instant expiresAt = claimsSet.getExpirationTime().toInstant();

                    Jwt jwt = new Jwt(token, claimsSet.getIssueTime().toInstant(), expiresAt, 
                                      Map.of("alg", signedJWT.getHeader().getAlgorithm().getName()), 
                                      claims);
                    return jwt;

                } catch (Exception e) {
                    throw new JwtException("Failed to decode JWT", e);
                }
            }
        };
    }

    @Bean
    public JwtAuthenticationConverter keycloakJwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakAuthorizationConverter(rptTokenFetcher()));

        return converter;
    }

    protected RptTokenFetcher rptTokenFetcher() {
        RptTokenFetcher rptTokenFetcher = new RptTokenFetcher(realmUrl, clientId, clientSecret);

        rptTokenFetcher.loadPathPrefixes(resourceSet);
        return rptTokenFetcher;
    }

    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> {
            log.info("Failed to {}", authException.getMessage(), authException);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");

            String json = String.format("""
                    {
                      "error": "unauthorized",
                      "message": "%s"
                    }
                    """, authException.getMessage());

            response.getWriter().write(json);
        };
    }

}