package com.M3Tours.operadores.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientConfig {
    @Bean
    public WebClient.Builder WebClientBuilder(){
        return  WebClient.builder();
    }

    @Bean
    public WebClient WebClientEmpresa(WebClient.Builder builder){
        return builder.baseUrl("localhost:8081/api/v1").build();
    }

    @Bean
    public WebClient WebClientUsuario(WebClient.Builder builder){
        return builder.baseUrl("localhost:8080/api/v1").build();
    }
}
