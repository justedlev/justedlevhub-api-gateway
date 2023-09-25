package com.justedlev.hub.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityWebFilterChain securityFilterChain(@NonNull ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .oauth2ResourceServer(oAuth2ResourceServerSpecCustomizer())
                .authorizeExchange(authorizeExchangeSpecCustomizer())
                .build();
    }

    @NonNull
    private Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> authorizeExchangeSpecCustomizer() {
        return exchangeSpec -> exchangeSpec
                .pathMatchers("/actuator/**", "/sso/logout").permitAll()
                .anyExchange().authenticated();
    }

    @NonNull
    private Customizer<ServerHttpSecurity.OAuth2ResourceServerSpec> oAuth2ResourceServerSpecCustomizer() {
        return oAuth -> oAuth.jwt(jwtSpecCustomizer());
    }

    @NonNull
    private Customizer<ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec> jwtSpecCustomizer() {
        return jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());
    }

    @Bean
    public ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        return new ReactiveJwtAuthenticationConverterAdapter(KeycloakJwtAuthenticationConverter.getInstance());
    }
}
