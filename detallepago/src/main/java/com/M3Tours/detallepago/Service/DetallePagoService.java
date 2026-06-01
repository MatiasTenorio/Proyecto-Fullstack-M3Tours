package com.M3Tours.detallepago.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.M3Tours.detallepago.DTO.DetallePagoDTO;
import com.M3Tours.detallepago.DTO.PagoDTO;
import com.M3Tours.detallepago.Model.DetallePago;
import com.M3Tours.detallepago.Repository.DetallePagoRepository;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class DetallePagoService {
    @Autowired
    private DetallePagoRepository repository;
 
    @Autowired
    @Qualifier("WebClientPagos")
    private WebClient webClientPagos;
 
    public List<DetallePago> findAll() {
        return repository.findAll();
    }
 
    public boolean save(DetallePagoDTO detallePagoDTO) {
        PagoDTO pago = webClientPagos.get()
                .uri("/pagos/{id}", detallePagoDTO.getId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new RuntimeException("Pago no encontrado")))
                .bodyToMono(PagoDTO.class)
                .block();
 
        if (pago == null) {
            return false;
        }
 
        DetallePago detallePago = new DetallePago();
        detallePago.setNumeroBoleta(detallePagoDTO.getNumeroBoleta());
        detallePago.setTipoPago(detallePagoDTO.getTipoPago());
        detallePago.setEstado(detallePagoDTO.getEstado());
        detallePago.setNombreTour(detallePagoDTO.getNombreTour());
        detallePago.setNumeroAsiento(detallePagoDTO.getNumeroAsiento());
        detallePago.setSubtotal(detallePagoDTO.getSubtotal());
        detallePago.setImpuesto(detallePagoDTO.getImpuesto());
        detallePago.setTotal(detallePagoDTO.getTotal());
 
        repository.save(detallePago);
        return true;
    }
 
    public Boolean delete(Integer id) {
        if (repository.findById(id).isEmpty()) {
            return false;
        }
        repository.delete(repository.findById(id).get());
        return true;
    }
 
    public Optional<DetallePago> findById(Integer id) {
        return repository.findById(id);
    }
 
    public Optional<DetallePago> findByNumeroBoleta(String numeroBoleta) {
        return repository.findByNumeroBoleta(numeroBoleta);
    }
 
    public Optional<DetallePago> findByTipoPago(String tipoPago) {
        return repository.findByTipoPago(tipoPago);
    }
 
    public Optional<DetallePago> findByEstado(String estado) {
        return repository.findByEstado(estado);
    }
 
    public Optional<DetallePago> findByNombreTour(String nombreTour) {
        return repository.findByNombreTour(nombreTour);
    }
 
}