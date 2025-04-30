package com.kassandra.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://localhost:5173",
                        "http://localhost:5174",
                        "http://localhost:5175",
                        "http://localhost:5176",
                        "http://localhost:5177",
                        "http://localhost:5178",
                        "http://localhost:5179",
                        "http://localhost:8080",
                        "http://localhost:5500",
                        "http://127.0.0.1:5173",
                        "http://127.0.0.1:5179",
                        "http://127.0.0.1:5500",
                        // Продакшн домены
                        "https://kassandra-treading.up.railway.app",
                        "https://kassandra-treading-frontend.up.railway.app"
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true) // Теперь можно использовать allowCredentials(true)
                .maxAge(3600);
    }
}
