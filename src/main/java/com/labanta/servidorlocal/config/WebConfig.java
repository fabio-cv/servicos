package com.labanta.servidorlocal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Sempre que alguém aceder ao URL "/imagens/qualquer-coisa.png"...
        registry.addResourceHandler("/imagens/**")
                // ...o Spring vai buscar o ficheiro fisicamente à nossa pasta "uploads/imagens/"
                .addResourceLocations("file:uploads/imagens/");
    }

}
