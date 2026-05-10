package com.eventx.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI eventXOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("EventX Manager API")
                        .version("2.1.0")
                        .description("API REST per la gestione della compravendita di biglietti per eventi."))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Token JWT ottenuto tramite login")))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"));
    }
}
