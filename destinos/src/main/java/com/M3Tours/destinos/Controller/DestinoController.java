package com.M3Tours.destinos.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.M3Tours.destinos.DTO.DestinoDTO;
import com.M3Tours.destinos.Model.Destino;
import com.M3Tours.destinos.Service.DestinoService;

@RestController
@RequestMapping("/api/destinos")
public class DestinoController {

    @Autowired
    private DestinoService destinoService;

    @GetMapping
    public ResponseEntity<List<Destino>> listarTodos() {
        return ResponseEntity.ok(destinoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Destino> buscarPorId(@PathVariable Integer id) {
        return destinoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Destino> crear(@RequestBody DestinoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(destinoService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Destino> actualizar(@PathVariable Integer id, @RequestBody DestinoDTO dto) {
        return destinoService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return destinoService.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
