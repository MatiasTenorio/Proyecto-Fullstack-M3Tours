package com.M3Tours.reserva.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean(name = "WebClientTours") 
    public WebClient webClientTours(WebClient.Builder builder) {

        return builder.baseUrl("http://localhost:8083/api/v1").build(); 
    }
}