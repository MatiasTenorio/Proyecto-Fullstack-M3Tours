package com.M3Tours.tour.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.M3Tours.tour.Model.Tour;

@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {

    Optional<Tour> findByOperadorId(Integer id);

    Optional<Tour> findByNumeroReservas(int reservas);

    Optional<Tour> findByUbicacionInicial(String ubicacion);
}