package com.justedlev.hub.configuration.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class KeycloakGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private final JwtGrantedAuthoritiesConverter origin = new JwtGrantedAuthoritiesConverter();
    private List<String> claimPaths = List.of("realm_access", "roles");

    /**
     * @param claimPath the path with dot separator, example {@code "path1.path2"}
     * @return this
     */
    public KeycloakGrantedAuthoritiesConverter setClaimPath(@NonNull String claimPath) {
        this.claimPaths = List.of(claimPath.split("\\."));
        return this;
    }

    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt source) {
        return Stream.concat(
                getFromOrigin(source),
                getRoles(source)
        ).toList();
    }

    private Stream<? extends GrantedAuthority> getFromOrigin(Jwt source) {
        return Optional.of(source).map(origin::convert).stream().flatMap(Collection::stream);
    }

    private Stream<? extends GrantedAuthority> getRoles(Jwt jwt) {
        var roles = extractClaimRoles(jwt);

        return roles.stream().map(SimpleGrantedAuthority::new);
    }

    private Collection<String> extractClaimRoles(Jwt jwt) {

        if (claimPaths.size() == 1) {
            return jwt.getClaimAsStringList(claimPaths.get(0));
        }

        var start = jwt.getClaimAsMap(claimPaths.get(0));

        for (int i = 1; i < claimPaths.size(); i++) {
            var path = claimPaths.get(i);

            if (i == claimPaths.size() - 1) {
                return castToCollectionRoles(start.get(path));
            }

            start = castToMap(start.get(path));
        }

        return List.of();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> castToMap(Object o) {
        return (Map<String, Object>) o;
    }

    @SuppressWarnings("unchecked")
    private Collection<String> castToCollectionRoles(Object o) {
        return (List<String>) o;
    }

    public static KeycloakGrantedAuthoritiesConverter getInstance() {
        return new KeycloakGrantedAuthoritiesConverter();
    }
}
