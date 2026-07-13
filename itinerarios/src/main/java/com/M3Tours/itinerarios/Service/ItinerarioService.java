package com.M3Tours.itinerarios.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.M3Tours.itinerarios.DTO.ItinerarioDTO;
import com.M3Tours.itinerarios.DTO.TourDTO;
import com.M3Tours.itinerarios.Model.Itinerario;
import com.M3Tours.itinerarios.Repository.ItinerarioRepository;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class ItinerarioService {

    @Autowired
    private ItinerarioRepository repository;
    @Autowired
    @Qualifier("WebClientTours")
    private WebClient webClientTours;

    public List<Itinerario> findAll(){
        return repository.findAll();
    }

    public Optional<Itinerario> findById(Integer id){
        return repository.findById(id);
    }

    public boolean save(ItinerarioDTO dto) {
        TourDTO tour = webClientTours.get()
                .uri("/tours/{id}", dto.getTourId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                    Mono.error(new RuntimeException("Tour no encontrado")))
                .bodyToMono(TourDTO.class)
                .block();
        if (tour == null) {
            return false;
        }

        Itinerario itinerario = new Itinerario();
        itinerario.setTourId(tour.getId());
        itinerario.setDestinoId(dto.getDestinoId());
        itinerario.setDia(dto.getDia());
        itinerario.setDescripcion(dto.getDescripcion());
        itinerario.setHoraInicio(dto.getHoraInicio());
        itinerario.setHoraFin(dto.getHoraFin());
        itinerario.setLugar(dto.getLugar());
        repository.save(itinerario);
        return true;
    }

    public boolean update(Integer id, ItinerarioDTO dto) {
        if(repository.findById(id).isEmpty()){
            return false;
        }
        Itinerario itinerario = repository.findById(id).get();
        itinerario.setDestinoId(dto.getDestinoId());
        itinerario.setDia(dto.getDia());
        itinerario.setDescripcion(dto.getDescripcion());
        itinerario.setHoraInicio(dto.getHoraInicio());
        itinerario.setHoraFin(dto.getHoraFin());
        itinerario.setLugar(dto.getLugar());
        repository.save(itinerario);
        return true;
    }

    public boolean delete(Integer id) {
        if(repository.existsById(id)){
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}