package com.hrms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // İzin verilen origin'ler
        config.addAllowedOrigin("http://localhost:8100");
        config.addAllowedOrigin("http://localhost:4200");
        
        // İzin verilen HTTP metodları
        config.addAllowedMethod("*");
        
        // İzin verilen header'lar
        config.addAllowedHeader("*");
        
        // Credentials izni (örn: cookies)
        config.setAllowCredentials(true);
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
} 