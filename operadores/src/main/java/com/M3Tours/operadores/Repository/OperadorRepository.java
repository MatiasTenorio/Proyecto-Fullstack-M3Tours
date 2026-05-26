package com.M3Tours.operadores.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.M3Tours.operadores.Model.Operador;

@Repository
public interface OperadorRepository extends JpaRepository<Operador, Integer> {
}