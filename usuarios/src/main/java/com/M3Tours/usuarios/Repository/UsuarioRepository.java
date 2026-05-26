package com.M3Tours.usuarios.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.M3Tours.usuarios.Model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
    
    Optional<Usuario> findByNombre(String nombre);

    @Query(value="Select user from usuarios user rut= :rut", nativeQuery = true)
    Optional<Usuario> findByRut(String rut);
}

