package com.justedlev.hub.configuration.security;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Setter(onParam = @__(@NonNull))
@RequiredArgsConstructor
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final KeycloakProperties.JwtConverterProperties properties;
    private final KeycloakGrantedAuthoritiesConverter keycloakGrantedAuthoritiesConverter;

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        var name = extractName(jwt);
        var authorities = keycloakGrantedAuthoritiesConverter.convert(jwt);

        return new JwtAuthenticationToken(jwt, authorities, name);
    }

    private String extractName(Jwt jwt) {
        return Optional.of(properties)
                .map(KeycloakProperties.JwtConverterProperties::getPrincipalClaimName)
                .map(jwt::getClaimAsString)
                .orElseGet(jwt::getSubject);
    }
}
