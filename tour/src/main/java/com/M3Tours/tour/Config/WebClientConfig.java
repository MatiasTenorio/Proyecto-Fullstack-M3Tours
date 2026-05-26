package com.M3Tours.tour.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient.Builder WebClientBuilder(){
        return WebClient.builder();
    }

    @Bean 
    public WebClient WebClientOperador(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8083/api/v1").build(); // Puerto del Micro de Paciente
    }
}
