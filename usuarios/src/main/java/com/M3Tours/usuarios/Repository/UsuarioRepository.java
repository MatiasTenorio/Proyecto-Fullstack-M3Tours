package com.M3Tours.usuarios.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.M3Tours.usuarios.Model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    @Query(value = "SELECT * FROM usuarios WHERE rut = :rut", nativeQuery = true)
    Optional<Usuario> findByRut(@Param("rut") String rut);
}