package dev.justedlev.hub;

import dev.justedlev.hub.configuration.properties.KeycloakClientProperties;
import dev.justedlev.hub.configuration.properties.KeycloakJwtConverterProperties;
import dev.justedlev.hub.configuration.properties.KeycloakProperties;
import dev.justedlev.hub.configuration.properties.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties({
        KeycloakProperties.class,
        KeycloakClientProperties.class,
        KeycloakJwtConverterProperties.class,
        SecurityProperties.class,
})
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

}
