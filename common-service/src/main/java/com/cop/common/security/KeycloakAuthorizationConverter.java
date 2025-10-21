package com.cop.common.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class KeycloakAuthorizationConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final RptTokenFetcher rptFetcher;

    public KeycloakAuthorizationConverter() {
        this.rptFetcher = null;
    }

    public KeycloakAuthorizationConverter(RptTokenFetcher rptFetcher) {
        this.rptFetcher = rptFetcher;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // 1. Realm roles
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            List<String> roles = (List<String>) realmAccess.get("roles");
            roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
        }

        // 2. Client roles
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null && resourceAccess.containsKey("myapp")) {
            Map<String, Object> client = (Map<String, Object>) resourceAccess.get("myapp");
            if (client != null && client.containsKey("roles")) {
                List<String> roles = (List<String>) client.get("roles");
                roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
            }
        }

        // 3. Groups
        List<String> groups = jwt.getClaimAsStringList("groups");
        if (groups != null) {
            for (String group : groups) {
                authorities.add(new SimpleGrantedAuthority("GROUP_" + group));
            }
        }

        // 4. RPT (access_token -> RPT)
        if (rptFetcher != null) {
            List<Map<String, Object>> permissions = rptFetcher.fetchRpt(jwt.getTokenValue());
            if (permissions != null) {
                for (Map<String, Object> permission : permissions) {
                    List<String> scopes = (List<String>) permission.get("scopes");
                    if (scopes != null) {
                        scopes.forEach(scope -> authorities.add(new SimpleGrantedAuthority("SCOPE_" + scope)));
                    }
                }
            }
        }

        return authorities;
    }
}
