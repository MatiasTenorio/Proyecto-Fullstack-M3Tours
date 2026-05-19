package com.M3Tours.usuarios.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.M3Tours.usuarios.DTO.UsuarioDTO;
import com.M3Tours.usuarios.Model.Usuario;
import com.M3Tours.usuarios.Service.UsuarioService;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService service;

     @PostMapping("/agregar-usuario")
    public ResponseEntity<String> save(@RequestBody UsuarioDTO usuario) {
        service.save(usuario);
        return ResponseEntity.ok("Usuario añadido con éxito.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {

        if (service.delete(id)) {
            return ResponseEntity.ok("Usuario eliminado con éxito");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Usuario con ID " + id + " no encontrado.");
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {

        Optional<Usuario> responseService = service.findById(id);

        if (responseService.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario con ID " + id + " no encontrado.");
        }

        return ResponseEntity.ok(responseService.get());
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> getByNombre(@PathVariable String nombre) {

        Optional<Usuario> responseService = service.findByNombre(nombre);

        if (responseService.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario con nombre '" + nombre + "' no encontrado.");
        }

        return ResponseEntity.ok(responseService.get());
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<?> getByRut(@PathVariable String rut) {

        Optional<Usuario> responseService = service.findByRut(rut);

        if (responseService.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario con RUT '" + rut + "' no encontrado.");
        }

        return ResponseEntity.ok(responseService.get());
    }
}
