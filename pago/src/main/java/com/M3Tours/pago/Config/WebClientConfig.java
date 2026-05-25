package com.M3Tours.pago.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient.Builder WebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient WebClientUsuarios(WebClient.Builder builder){
        return builder.baseUrl("http://localhost:8080/api/v1").build();
    }

    @Bean
    public WebClient WebClientReservas(WebClient.Builder builder){
        return builder.baseUrl("http://localhost:8085/api/v1").build();
    }
}
