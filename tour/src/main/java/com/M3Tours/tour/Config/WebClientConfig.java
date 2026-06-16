package com.M3Tours.tour.Config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.M3Tours.tour.Model.Tour;
import com.M3Tours.tour.Repository.TourRepository;

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

    // Datos creados temporalmente debido a que el microservicio "Operadores" se encuentra buggeado (Por ahora).
    @Bean
    CommandLineRunner initDatabase(TourRepository repository) {
        return args -> {

            // Solo inserta si la tabla está vacía para no duplicar datos cada vez que reinicias
            if (repository.count() == 0) {

                Tour tour1 = new Tour(null, 999, 0, "Puerto Montt", LocalDate.now().plusDays(10));
                Tour tour2 = new Tour(null, 999, 5, "Puerto Montt", LocalDate.now().plusDays(15));
                Tour tour3 = new Tour(null, 888, 12, "Puerto Montt", LocalDate.now().plusDays(20));

                repository.save(tour1);
                repository.save(tour2);
                repository.save(tour3);

                System.out.println("Datos de prueba de Tours cargados exitosamente.");
            }
        };
    }
}

