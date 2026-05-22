package com.M3Tours.detallepago.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean// anotacion para 
    public WebClient WebClientPagos(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8086/api/v1").build(); // Puerto del Micro de Paciente
    }
}
