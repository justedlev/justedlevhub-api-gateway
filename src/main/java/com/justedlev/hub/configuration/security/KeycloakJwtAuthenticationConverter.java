package com.justedlev.hub.configuration.security;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Setter(onParam = @__(@NonNull))
@Accessors(chain = true)
public class KeycloakJwtAuthenticationConverter extends JwtAuthenticationConverter {
    private String keycloakPrincipalClaimName = "preferred_username";
    private KeycloakGrantedAuthoritiesConverter keycloakGrantedAuthoritiesConverter =
            new KeycloakGrantedAuthoritiesConverter();

    public KeycloakJwtAuthenticationConverter() {
        super();
        setJwtGrantedAuthoritiesConverter(keycloakGrantedAuthoritiesConverter);
        setPrincipalClaimName(keycloakPrincipalClaimName);
    }
}
