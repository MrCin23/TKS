package pl.lodz.p.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Match all endpoints, including /REST/api/**
                .allowedOrigins("*") // https://localhost", "http://localhost","http://localhost:8080", "http://192.168.1.105", "http://192.168.56.1", "https://192.168.1.105", "https://192.168.56.1
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow these HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true) // Enable credentials (cookies, auth headers)
                .maxAge(3600); // Cache the CORS configuration for 1 hour
    }
}
