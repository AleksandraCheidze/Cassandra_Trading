package com.kassandra.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Cassandra Trading API",
                version = "1.0",
                description = "API documentation for Cassandra Trading Platform",
                contact = @Contact(
                        name = "Cassandra Support",
                        email = "support@kassandra.com",
                        url = "https://cassandra.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8000",
                        description = "Development Server"
                ),
                @Server(
                        url = "https://kassandra-treading.up.railway.app",
                        description = "Railway Production Server"
                ),
                @Server(
                        url = "https://cassandra-trading-1.onrender.com",
                        description = "Render Production Server"
                )
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
    // Configuration class for OpenAPI documentation
}
