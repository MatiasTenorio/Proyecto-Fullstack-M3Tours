package com.M3Tours.operadores.Controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.M3Tours.operadores.Model.Operador;
import com.M3Tours.operadores.Service.OperadorService;

@RestController
@RequestMapping("/api/v1/operadores")
public class OperadorController {
    
    private static final Logger log = LoggerFactory.getLogger(OperadorController.class);

    @Autowired
    private OperadorService service;
    
    @GetMapping // GET ALL
    public ResponseEntity<?> findAll() {
        if (service.findAll().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay operadores en este momento");
        }
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}") // GET BY {ID}
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        Optional<Operador> operador = service.findById(id);
        if (operador.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe operador con id '" + id + "'");
        }
        return ResponseEntity.ok(operador.get());
    }

    @GetMapping("/nombre/{nombre}") // GET BY {NOMBRE}
    public ResponseEntity<?> findByNombre(@PathVariable String nombre) {
        Optional<Operador> operador = service.findByNombre(nombre);
        if (operador.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe operador con nombre '" + nombre + "'");
        }
        return ResponseEntity.ok(operador.get());
    }

    @GetMapping("/rut/{rut}") // GET BY {RUT]}
    public ResponseEntity<?> findByRut(@PathVariable String rut) {
        Optional<Operador> operador = service.findByRut(rut);
        if (operador.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe operador con rut '" + rut + "'");
        }
        return ResponseEntity.ok(operador.get());
    }

    @GetMapping("/email/{email}") // GET BY {EMAIL}
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        Optional<Operador> operador = service.findByEmail(email);
        if (operador.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(operador.get());
    }

    @PostMapping("/agregar-operador") // POST OPERADOR
    public ResponseEntity<?> save(@RequestBody OperadorDTO DTO) {
        if (!service.save(DTO)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa o Usuario no encontrado");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Operador creado con exito!");
    }

    @PutMapping("/{id}") // ACUTALIZAR OPERADOR
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody OperadorDTO DTO) {
        if (service.findById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe operador con id '" + id + "'");
        }
        return ResponseEntity.ok(service.update(id, DTO));
    }

    @DeleteMapping("/{id}") // DELETE BY ID
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        if (service.findById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe operador con id '" + id + "'");
        }
        return ResponseEntity.ok(service.delete(id));
    }
}
