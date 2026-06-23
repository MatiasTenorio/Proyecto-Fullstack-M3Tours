package com.M3Tours.categoria.Controller;

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

import com.M3Tours.categoria.DTO.CategoriaDTO;
import com.M3Tours.categoria.Service.CategoriaService;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @GetMapping("") // GET ALL
    public ResponseEntity<?> findAll() {
        if (service.findAll().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay categorias en este momento");
        }
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}") // GET BY ID
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        if (service.findById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe categoria con id '" + id + "'");
        }
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("") // POST CATEGORIA
    public ResponseEntity<?> crear(@RequestBody CategoriaDTO DTO) {
        service.save(DTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Categoria creada con exito!");
    }

    @PutMapping("/{id}") // UPDATE CATEGORIA
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody CategoriaDTO DTO) {
        if (!service.update(id, DTO)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe categoria con id '" + id + "'");
        }
        return ResponseEntity.ok("Categoria actualizada con exito!");
    }

    @DeleteMapping("/{id}") // DELETE CATEGORIA
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        if (service.findById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe categoria con id '" + id + "'");
        }
        service.delete(id);
        return ResponseEntity.ok("Categoria eliminada exitosamente");
    }
}