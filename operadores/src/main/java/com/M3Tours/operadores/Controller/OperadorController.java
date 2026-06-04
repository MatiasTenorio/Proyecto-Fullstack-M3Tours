package com.M3Tours.operadores.Controller;

import java.util.List;
import java.util.Optional;

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
@Autowired
    private OperadorService operadorService;

    @GetMapping("")
    public ResponseEntity<List<Operador>> findAll() {
        return ResponseEntity.ok(operadorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        Optional<Operador> operador = operadorService.findById(id);
        if(operador.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario o Empresa no encontrados");
        }
        return ResponseEntity.ok(operador.get());
    }

    @PostMapping("")
    public ResponseEntity<String> save(@RequestBody OperadorDTO dto) {
        Boolean save = operadorService.save(dto);
        if(save!=true){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Error: Operador no encontrado.");
        }
        return ResponseEntity.ok("Operador guardado con exito!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody OperadorDTO dto) {
        Boolean update = operadorService.update(id, dto);
        if(update!=true){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Operador con id '" + id + "' no encontrado");
        }
        return ResponseEntity.ok("Operador actualizado con exito!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        boolean deleted = operadorService.delete(id);
        if(deleted){
            return ResponseEntity.ok("Operador eliminado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body("Operador con id '" + id + "' no encontrado");
    }
}
