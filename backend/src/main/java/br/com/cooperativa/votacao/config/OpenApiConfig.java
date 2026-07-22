package br.com.cooperativa.votacao.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Votação - Cooperativa")
                        .version("1.0.0")
                        .description("API REST para gerenciamento de sessões de votação em cooperativas")
                        .contact(new Contact()
                                .name("Equipe de Desenvolvimento")
                                .email("dev@cooperativa.com.br")));
    }
}
