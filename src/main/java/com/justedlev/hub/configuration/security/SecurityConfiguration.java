package com.justedlev.hub.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final KeycloakLogoutHandler keycloakLogoutHandler;

    @Bean
    public SecurityWebFilterChain securityFilterChain(@NonNull ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(logoutHandler -> logoutHandler
                        .logoutHandler(keycloakLogoutHandler)
                        .logoutUrl("/logout")
                )
                .authorizeExchange(exchangeSpec -> exchangeSpec
                        .pathMatchers(
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui.html",
                                "/webjars/**",
                                "/v3/api-docs/**",
                                "/api/public/**",
                                "/api/public/authenticate",
                                "/actuator/**",
                                "/*/actuator/**",
                                "/swagger-ui/**",
                                "/error",
                                "/sso/logout",
                                "/logout"
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(serverSpec -> serverSpec.jwt(Customizer.withDefaults()))
                .build();
    }
}
