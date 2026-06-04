package com.M3Tours.operadores.Controller;

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

import com.M3Tours.operadores.DTO.OperadorDTO;
import com.M3Tours.operadores.Service.OperadorService;

@RestController
@RequestMapping("/api/v1/operadores")
public class OperadorController {

    @Autowired
    private OperadorService service;

    @GetMapping
    public ResponseEntity<?> findAll() {
        if (service.findAll().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay operadores en este momento");
        }
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        if (service.findById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe operador con id '" + id + "'");
        }
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody OperadorDTO DTO) {
        if (!service.save(DTO)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa o Usuario no encontrado");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(DTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody OperadorDTO DTO) {
        if (service.findById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe operador con id '" + id + "'");
        }
        return ResponseEntity.ok(service.update(id, DTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        if (service.findById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe operador con id '" + id + "'");
        }
        return ResponseEntity.ok(service.delete(id));
    }
}
