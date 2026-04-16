package com.hariomclinic.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SwaggerConfig - Configures OpenAPI/Swagger documentation.
 *
 * Swagger UI is accessible at: http://localhost:8080/swagger-ui.html
 *
 * SecurityScheme -> Adds "Authorize" button in Swagger UI.
 *   After login, paste the JWT token in Swagger to test protected endpoints.
 *   Format: Bearer <your-jwt-token>
 *
 * This config adds JWT Bearer auth to ALL endpoints in Swagger.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hari Om Clinic API")
                        .description("REST API for Hari Om Homeopathic Clinic - Patient Management System")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Dr. Kalpesh Pashte")
                                .email("hariomclinic7746@gmail.com")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token obtained from /api/auth/login")));
    }
}
