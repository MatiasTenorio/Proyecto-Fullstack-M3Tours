package com.M3Tours.itinerarios.Controller;

import com.M3Tours.itinerarios.DTO.ItinerarioDTO;
import com.M3Tours.itinerarios.Model.Itinerario;
import com.M3Tours.itinerarios.Service.ItinerarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itinerarios")
public class ItinerarioController {

    @Autowired
    private ItinerarioService itinerarioService;

    @GetMapping
    public ResponseEntity<List<Itinerario>> listarTodos() {
        return ResponseEntity.ok(itinerarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Itinerario> buscarPorId(@PathVariable Integer id) {
        return itinerarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Itinerario> crear(@RequestBody ItinerarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itinerarioService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Itinerario> actualizar(@PathVariable Integer id, @RequestBody ItinerarioDTO dto) {
        return itinerarioService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return itinerarioService.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}