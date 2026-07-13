package com.M3Tours.reserva.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.M3Tours.reserva.Model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
 
    Optional<Reserva> findByTourId(Integer tourId);
 
}
 