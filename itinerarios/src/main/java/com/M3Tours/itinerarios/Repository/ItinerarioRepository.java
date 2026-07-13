package com.M3Tours.itinerarios.Repository;

import com.M3Tours.itinerarios.Model.Itinerario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItinerarioRepository extends JpaRepository<Itinerario, Integer> {
}