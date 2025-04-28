package com.encontreaqui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
  
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Aplica para todos os endpoints
                .allowedOriginPatterns("*")  // Permite qualquer origem
                .allowedMethods("*")         // Permite todos os métodos (GET, POST, PUT, DELETE, OPTIONS, etc.)
                .allowedHeaders("*")         // Permite todos os headers
                .allowCredentials(true);     // Se precisar enviar credenciais (cookies, etc.)
    }
}
