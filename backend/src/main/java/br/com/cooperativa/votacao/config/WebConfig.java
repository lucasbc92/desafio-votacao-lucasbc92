package br.com.cooperativa.votacao.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração para desabilitar o tratamento de recursos estáticos
 * e evitar erros ao acessar URLs da API diretamente no navegador.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Desabilita completamente o handler de recursos estáticos
        // para evitar conflitos com as rotas da API REST
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(false);
    }
}