package com.M3Tours.reserva.Controller;

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

import com.M3Tours.reserva.DTO.ReservaDTO;
import com.M3Tours.reserva.Model.Reserva;
import com.M3Tours.reserva.Service.ReservaService;

@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaController {
 
    @Autowired
    private ReservaService service;
 
    @GetMapping // GET ALL
    public ResponseEntity<List<Reserva>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
 
    @GetMapping("/{id}") // GET BY ID
    public ResponseEntity<Reserva> getById(@PathVariable Integer id) {
        Optional<Reserva> reserva = service.findById(id);
        if (reserva.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(reserva.get());
    }
 
    @GetMapping("/tour/{tourId}") // GET BY TOUR_ID
    public ResponseEntity<Reserva> getByTourId(@PathVariable Integer tourId) {
        Optional<Reserva> reserva = service.findByTourId(tourId);
        if (reserva.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(reserva.get());
    }
 
    @PostMapping("agregar-reserva") // POST RESERVA
    public ResponseEntity<String> save(@RequestBody ReservaDTO reserva) {
        Boolean save = service.save(reserva);
        if (save != true) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Tour correspondiente no existe.");
        }
        return ResponseEntity.ok("Reserva guardada con exito!");
    }
 
    @DeleteMapping("/{id}") // DELETE BY ID
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Reserva eliminada exitosamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva con id '" + id + "' no encontrada");
    }
 
}