package com.justedlev.hub;

import com.justedlev.hub.configuration.properties.KeycloakClientProperties;
import com.justedlev.hub.configuration.properties.KeycloakJwtConverterProperties;
import com.justedlev.hub.configuration.properties.KeycloakProperties;
import com.justedlev.hub.configuration.properties.SecurityProperties;
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
