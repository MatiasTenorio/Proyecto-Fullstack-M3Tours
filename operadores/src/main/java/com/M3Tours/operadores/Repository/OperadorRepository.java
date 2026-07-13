package com.M3Tours.operadores.Repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.M3Tours.operadores.Model.Operador;
@Repository
public interface OperadorRepository extends JpaRepository<Operador, Integer> {
    Optional<Operador> findByNombre(String nombre);

    @Query(value = "SELECT * FROM operadores WHERE rut = :rut", nativeQuery = true)
    Optional<Operador> findByRut(@Param("rut") String rut);

    @Query(value = "SELECT * FROM operadores WHERE email = :email", nativeQuery = true)
    Optional<Operador> findByEmail(@Param("email") String email);
}