package com.cop.common.security;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.cop.common.util.InsecureRestTemplateFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Keycloak RS256 공개키 제공자
 * - JWT kid와 매칭된 키만 사용
 * - 키 캐시 + 캐시 만료 후 자동 갱신 지원
 */
public class KeycloakPublicKeyProvider {

    private final String jwksUrl;
    private final long cacheDurationSeconds;
    private Map<String, RSAPublicKey> cachedKeys = new HashMap<>();
    private Instant lastFetchTime = Instant.EPOCH;

    public KeycloakPublicKeyProvider(String realmUrl) {
        this.jwksUrl = realmUrl + "/protocol/openid-connect/certs";
        this.cacheDurationSeconds = 30;
        fetchKeysFromJwks(); // 초기 JWKS 조회
    }

    /**
     * JWT kid에 맞는 공개키 반환
     */
    public RSAPublicKey getKeyByKid(String kid) {
        // 캐시 만료 확인
        if (Instant.now().isAfter(lastFetchTime.plusSeconds(cacheDurationSeconds))) {
            fetchKeysFromJwks();
        }

        RSAPublicKey key = cachedKeys.get(kid);
        if (key == null) {
            throw new RuntimeException("No matching public key found for kid: " + kid);
        }
        return key;
    }

    /**
     * JWKS에서 공개키를 가져와 캐시에 저장
     */
    private synchronized void fetchKeysFromJwks() {
        try {
            RestTemplate restTemplate = InsecureRestTemplateFactory.create();
            ObjectMapper objectMapper = new ObjectMapper();

            String jwksJson = restTemplate.getForObject(jwksUrl, String.class);
            JsonNode jwks = objectMapper.readTree(jwksJson);

            Map<String, RSAPublicKey> newKeys = new HashMap<>();
            for (JsonNode keyNode : jwks.get("keys")) {
                String kid = keyNode.get("kid").asText();
                String alg = keyNode.get("alg").asText();
                String use = keyNode.has("use") ? keyNode.get("use").asText() : "";

                // 서명용 키(RS256)만 처리
                if (!"RS256".equals(alg) || !"sig".equals(use)) {
                    continue;
                }

                String n = keyNode.get("n").asText();
                String e = keyNode.get("e").asText();

                BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(n));
                BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(e));

                RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(spec);

                newKeys.put(kid, publicKey);
            }

            if (newKeys.isEmpty()) {
                throw new RuntimeException("No valid RS256 signing keys found in JWKS");
            }

            cachedKeys = newKeys;
            lastFetchTime = Instant.now();

        } catch (Exception ex) {
            throw new RuntimeException("Failed to fetch or parse JWKS", ex);
        }
    }
}
