package com.M3Tours.detallepago.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.M3Tours.detallepago.Model.DetallePago;

@Repository
public interface DetallePagoRepository extends JpaRepository<DetallePago, Integer> {
 
    Optional<DetallePago> findByNumeroBoleta(String numeroBoleta);
 
    Optional<DetallePago> findByTipoPago(String tipoPago);
 
    Optional<DetallePago> findByEstado(String estado);
 
    Optional<DetallePago> findByNombreTour(String nombreTour);
 
}