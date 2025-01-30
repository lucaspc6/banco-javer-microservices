package com.example.clienteAPI.config;

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
                            .title("Microsserviços em Nuvem - Cliente API")
                            .description("Primeira Aplicação: Ela faz requisições REST para a segunda aplicação, expõe quatro endpoints CRUD e um adicional para calcular o score de crédito (saldo_cc * 0,1) com base nas informações da segunda aplicação.")
                            .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                );
    }
}

