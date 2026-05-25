package com.M3Tours.destinos.Repository;

import com.M3Tours.destinos.Model.Destino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinoRepository extends JpaRepository<Destino, Integer> {
}
