package com.M3Tours.reserva.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.M3Tours.reserva.DTO.ReservaDTO;
import com.M3Tours.reserva.DTO.TourDTO;
import com.M3Tours.reserva.Model.Reserva;
import com.M3Tours.reserva.Repository.ReservaRepository;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class ReservaService {
 
    @Autowired
    private ReservaRepository repository;
 
    @Autowired
    @Qualifier("WebClientTours")
    private WebClient webClientTours;
 
    public List<Reserva> findAll() {
        return repository.findAll();
    }
 
    public boolean save(ReservaDTO reservaDTO) {
        TourDTO tour = webClientTours.get()
                .uri("/tours/{id}", reservaDTO.getTourId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new RuntimeException("Tour no encontrado")))
                .bodyToMono(TourDTO.class)
                .block();
 
        if (tour == null) {
            return false;
        }
 
        Reserva reserva = new Reserva();
        reserva.setTourId(tour.getId());
        reserva.setNumeroAsiento(reservaDTO.getNumeroAsiento());
 
        repository.save(reserva);
        return true;
    }
 
    public Boolean delete(Integer id) {
        if (repository.findById(id).isEmpty()) {
            return false;
        }
        repository.delete(repository.findById(id).get());
        return true;
    }
 
    public Optional<Reserva> findById(Integer id) {
        return repository.findById(id);
    }
 
    public Optional<Reserva> findByTourId(Integer tourId) {
        return repository.findByTourId(tourId);
    }
 
}