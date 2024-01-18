package com.serenitydojo.wordle.microservices.oauth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // You can customize the path pattern and allowed origins as needed
                registry.addMapping("/api/**")
                        .allowedOrigins("http://127.0.0.1:5173") // Add your front-end origin here
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Specify the HTTP methods allowed
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true); // Include cookies in the request if needed
            }
        };
    }
}