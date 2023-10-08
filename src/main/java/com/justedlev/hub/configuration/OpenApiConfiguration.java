package com.justedlev.hub.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "API Gateway",
                description = "JustedlevHub APIs documentation",
                version = "1.0",
                contact = @Contact(
                        name = "JustedlevHub",
                        email = "justedlevhub@gmail.com"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8765",
                        description = "Local ENV"
                ),
                @Server(
                        url = "http://api-gateway:8765",
                        description = "Docker ENV"
                )
        }
)
public class OpenApiConfiguration {
}
