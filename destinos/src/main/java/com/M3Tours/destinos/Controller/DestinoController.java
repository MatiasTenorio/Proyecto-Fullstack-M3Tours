package com.M3Tours.destinos.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.M3Tours.destinos.DTO.DestinoDTO;
import com.M3Tours.destinos.Service.DestinoService;

@RestController
@RequestMapping("/api/v1/destinos")
public class DestinoController {

    @Autowired
    private DestinoService service;

    @GetMapping // GET ALL
    public ResponseEntity<?> findAll() {
        if (service.findAll().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay destinos en este momento");
        }
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}") // GET BY ID
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        if (service.findById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe destino con id '" + id + "'");
        }
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("") // POST DESTINO
    public ResponseEntity<?> crear(@RequestBody DestinoDTO DTO) {
        service.save(DTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Destino creado con exito!");
    }

    @PutMapping("/{id}") // UPDATE DESTINO
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody DestinoDTO DTO) {
        if (!service.update(id, DTO)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe destino con id '" + id + "'");
        }
        return ResponseEntity.ok("Destino actualizado con exito!");
    }

    @DeleteMapping("/{id}") // DESLETE DESTINO
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        if (service.findById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe destino con id '" + id + "'");
        }
        service.delete(id);
        return ResponseEntity.ok("Destino eliminado exitosamente");
    }
}
