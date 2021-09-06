package com.predrag.a.postservice.config;

import com.predrag.a.jwt.service.JwtService;
import com.predrag.a.jwt.service.impl.DefaultJwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${config.jwt.secret.apiKey}")
    private String jwtSecret;

    @Bean
    public JwtService jwtService() {
        return new DefaultJwtService(jwtSecret);
    }
}