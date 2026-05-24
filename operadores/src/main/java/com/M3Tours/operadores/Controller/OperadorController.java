package com.M3Tours.operadores.Controller;

import java.util.List;

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
@RequestMapping("/api/operadores")
public class OperadorController {

    @Autowired
    private OperadorService operadorService;

    @GetMapping
    public ResponseEntity<List<Operador>> listarTodos() {
        return ResponseEntity.ok(operadorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Operador> buscarPorId(@PathVariable Integer id) {
        return operadorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Operador> crear(@RequestBody OperadorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(operadorService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Operador> actualizar(@PathVariable Integer id, @RequestBody OperadorDTO dto) {
        return operadorService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return operadorService.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
