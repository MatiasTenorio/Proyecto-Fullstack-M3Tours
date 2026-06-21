package com.M3Tours.itinerarios.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.M3Tours.itinerarios.DTO.ItinerarioDTO;
import com.M3Tours.itinerarios.Service.ItinerarioService;

@RestController
@RequestMapping("/api/v1/itinerarios")
public class ItinerarioController {

    @Autowired
    private ItinerarioService service;

    @GetMapping("")
    public ResponseEntity<?> findAll() {
        if (service.findAll().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay itinerarios en este momento");
        }
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        if (service.findById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe itinerario con id '" + id + "'");
        }
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> crear(@RequestBody ItinerarioDTO DTO) {
        if (!service.save(DTO)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tour no encontrado");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Itinerario creado con exito!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ItinerarioDTO DTO) {
        if (!service.update(id, DTO)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe itinerario con id '" + id + "'");
        }
        return ResponseEntity.ok("Itinerario actualizado con exito!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        if (service.findById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe itinerario con id '" + id + "'");
        }
        service.delete(id);
        return ResponseEntity.ok("Itinerario eliminado exitosamente");
    }
}