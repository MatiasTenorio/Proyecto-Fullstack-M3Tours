package com.M3Tours.tour.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.M3Tours.tour.DTO.TourDTO;
import com.M3Tours.tour.Model.Tour;
import com.M3Tours.tour.Service.TourService;

@RestController
@RequestMapping("/api/v1/tours")
public class TourController {

    @Autowired
    private TourService service;

    @GetMapping // GET ALL
    public ResponseEntity<List<Tour>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}") // GET BY ID
    public ResponseEntity<Tour> getById(@PathVariable Integer id) {
        Optional<Tour> tour = service.findById(id);
        if(tour.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(tour.get());
    }

    @GetMapping("/operador/{id}") // GET BY OPERADOR_ID
    public ResponseEntity<Tour> getByOperadorId(@PathVariable Integer id) {
        Optional<Tour> tour = service.findByOperadorId(id);
        if(tour.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(tour.get());
    }

    @GetMapping("/reservas/{reservas}") // GET BY NUMERO_RESERVAS
    public ResponseEntity<Tour> getByNumeroReservas(@PathVariable int reservas) {
        Optional<Tour> tour = service.findByNumeroReservas(reservas);
        if(tour.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(tour.get());
    }

    @GetMapping("/ubicacion/{ubicacion}") // GET BY UBICACION_INICIAl
    public ResponseEntity<Tour> getByUbicacionInicial(@PathVariable String ubicacion) {
        Optional<Tour> tour = service.findByUbicacionInicial(ubicacion);
        if(tour.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(tour.get());
    }

    @PostMapping("/agregar-tour") // POST TOUR
    public ResponseEntity<String> save(@RequestBody TourDTO tour) {
        Boolean save = service.save(tour);
        if(save!=true){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Error: Operador correspondiente no existe.");
        }
        return ResponseEntity.ok("Tour guardado con exito!");
    }

    @DeleteMapping("/{id}") // DELETE BY ID
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Tour eliminado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tour con id '" + id + "' no encontrado");
    }
}