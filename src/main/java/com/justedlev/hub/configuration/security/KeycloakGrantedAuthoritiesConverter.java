package com.justedlev.hub.configuration.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class KeycloakGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final List<String> claimPaths = List.of(
            "realm_access.roles",
            "resource_access.account.roles"
    );

    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        log.debug("Applying {}", jwtGrantedAuthoritiesConverter.getClass());

        return Stream.concat(
                Optional.of(jwt).map(jwtGrantedAuthoritiesConverter::convert).stream().flatMap(Collection::stream),
                extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet());
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        return claimPaths.stream()
                .map(path -> extractAuthority(jwt, path))
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @SuppressWarnings("unchecked")
    private Collection<String> extractAuthority(Jwt jwt, String path) {
        log.debug("Extracting from claim path: {}", path);

        if (!path.contains(".")) {
            return jwt.getClaimAsStringList(path);
        }

        var paths = path.split("\\.");
        var start = jwt.getClaimAsMap(paths[0]);

        for (int i = 1; i < paths.length; i++) {

            if (i == paths.length - 1) {
                return (List<String>) start.get(paths[i]);
            }

            start = (Map<String, Object>) start.get(paths[i]);
        }

        return List.of();
    }
}
