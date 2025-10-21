package com.cop.common.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.cop.common.util.InsecureRestTemplateFactory;
import com.cop.common.util.Trie;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RptTokenFetcher {

    private final RestTemplate restTemplate;
    private final String tokenEndpoint;
    private final String clientId;
    private final String clientSecret;

    private Trie resourceSet = new Trie();

    public RptTokenFetcher(String realmUrl, String clientId, String clientSecret) {
        this.tokenEndpoint = realmUrl + "/protocol/openid-connect/token";
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.restTemplate = InsecureRestTemplateFactory.create();
    }

    public void loadPathPrefixes(String[] resourceSet) {
        if (resourceSet == null)
            return;
        for (String prefix : resourceSet) {
            this.resourceSet.insert(prefix.trim());
        }
    }

    public List<Map<String, Object>> fetchRpt(String accessToken) {
        String path = RequestContextFilter.getCurrentRequestPath();

        try {
            String resourceName = resourceSet.longestPrefixMatch(path);
            if (resourceName == null) {
                log.info("No matching resource prefix found for path: {}", path);
                return null;
            }

            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBearerAuth(accessToken);

            // Prepare request body
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "urn:ietf:params:oauth:grant-type:uma-ticket");
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret);
            body.add("audience", clientId);
            body.add("response_mode", "permissions");
            body.add("submit_request", "true");
            body.add("permission", resourceName);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            // Send POST request
            ResponseEntity<String> response = restTemplate.postForEntity(tokenEndpoint, request, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new IllegalStateException("Failed to obtain RPT. Status: " + response.getStatusCode());
            }
            log.info("Found permissions for {}: {}", path, response.getBody());

            // Parse JWT from response
            JSONArray permissionsArray = new JSONArray(response.getBody());
            List<Map<String, Object>> permissions = new ArrayList<>();

            for (int i = 0; i < permissionsArray.length(); i++) {
                JSONObject obj = permissionsArray.getJSONObject(i);
                permissions.add(obj.toMap());
            }

            // Decode JWT
            return permissions;

        } catch (Exception e) {
            log.info("Fail to fetch RPT for {}: {}", path, e.getMessage());

            return null;
        }
    }

}
