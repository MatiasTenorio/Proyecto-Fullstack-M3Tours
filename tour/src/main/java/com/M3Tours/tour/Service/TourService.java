package com.M3Tours.tour.Service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.M3Tours.tour.DTO.TourDTO;
import com.M3Tours.tour.DTO.OperadorDTO;
import com.M3Tours.tour.Model.Tour;
import com.M3Tours.tour.Repository.TourRepository;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class TourService {

    @Autowired
    private TourRepository repository;
    @Autowired
    @Qualifier("WebClientOperador")
    private WebClient webClientOperador;

    public List<Tour> findAll(){
        return repository.findAll();
    }

    public boolean save(TourDTO tourDTO) {
        OperadorDTO operador = webClientOperador.get()
                .uri("/operadores/{id}", tourDTO.getOperadorId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                    Mono.error(new RuntimeException("Operador no encontrado")))
                .bodyToMono(OperadorDTO.class)
                .block();
        if (operador == null) {
            return false;
        }

        Tour tour = new Tour();
        tour.setOperadorId(operador.getId());
        tour.setNumeroReservas(tourDTO.getNumeroReservas());
        tour.setUbicacionInicial(tourDTO.getUbicacionInicial());
        tour.setFechaInicial(tourDTO.getFechaInicial());

        repository.save(tour);
        return true;
    }

    public Boolean delete(Integer id){
        if (repository.findById(id).isEmpty()){
            return false;
        }
        repository.delete(repository.findById(id).get());
        return true;
    }

    public Optional<Tour> findById(Integer id){
        return repository.findById(id);
    }

    public Optional<Tour> findByOperadorId(Integer id){
        return repository.findByOperadorId(id);
    }

    public Optional<Tour> findByNumeroReservas(int reservas){
        return repository.findByNumeroReservas(reservas);
    }

    public Optional<Tour> findByUbicacionInicial(String ubicacion){
        return repository.findByUbicacionInicial(ubicacion);
    }
}