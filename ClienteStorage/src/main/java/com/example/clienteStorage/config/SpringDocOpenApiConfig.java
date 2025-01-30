package com.example.clienteStorage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SpringDocOpenApiConfig {

    @Bean
    public OpenAPI openapi() {
        return new OpenAPI()
                .info(
                        new Info()
                            .title("Microsserviços em Nuvem - Cliente Storage")
                            .description("Segunda Aplicação: Ela é o coração do sistema, responsável por realizar operações CRUD em uma base de dados local H2.")
                            .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                );
    }
}
