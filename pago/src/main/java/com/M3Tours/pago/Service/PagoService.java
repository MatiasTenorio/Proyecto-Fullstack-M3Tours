package com.M3Tours.pago.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.M3Tours.pago.DTO.PagoDTO;
import com.M3Tours.pago.DTO.ReservaDTO;
import com.M3Tours.pago.DTO.UsuarioDTO;
import com.M3Tours.pago.Model.Pago;
import com.M3Tours.pago.Repository.PagoRepository;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class PagoService {
    @Autowired
    private PagoRepository repository;
    @Autowired
    @Qualifier("WebClienteUsuarios")
    private WebClient webClienteUsuarios;
    @Autowired
    @Qualifier("WebClienteReservas")
    private WebClient webClienteReservas;

    public List<Pago> findAll(){
        return repository.findAll();
    }
    
    public boolean save(PagoDTO pagoDTO) {
        UsuarioDTO usuario = webClienteUsuarios.get()
                .uri("/usuarios/{id}", pagoDTO.getUsuarioId()) 
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> 
                    Mono.error(new RuntimeException("Usuario no encontrado")))
                .bodyToMono(UsuarioDTO.class) 
                .block(); // Espera la respuesta aquí
        if (usuario == null) {
            return false;
        }
        ReservaDTO reserva = webClienteReservas.get()
                .uri("/reservas/{id}", pagoDTO.getReservaId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> 
                    Mono.error(new RuntimeException("Reserva no encontrado")))
                .bodyToMono(ReservaDTO.class) 
                .block();
        if (reserva == null) {
            return false;
        }

        Pago Pago = new Pago();
        Pago.setOrdenCompra(pagoDTO.getOrdenCompra());
        Pago.setCosto(pagoDTO.getCosto());
        Pago.setFechaPago(pagoDTO.getFechaPago());
        Pago.setFechaEmision(pagoDTO.getFechaEmision());

        Pago.setUsuarioId(usuario.getId());
        Pago.setReservaId(reserva.getId());
        repository.save(Pago);
        return true;
    }

    public Boolean delete(Integer id){
        if (repository.findById(id).isEmpty()){
            return false;
        }
        repository.delete(repository.findById(id).get());
        return true;
    }

    public Optional<Pago> findById(Integer id){
        return repository.findById(id);
    }

    public Optional<Pago> findByUserId(Integer id){
        return repository.findByUserId(id);
    }

    public Optional<Pago> findByReservaId(Integer id){
        return repository.findByReservaId(id);
    }
}
