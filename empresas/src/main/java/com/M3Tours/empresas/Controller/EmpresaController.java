package com.M3Tours.empresas.Controller;

import java.util.List;

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

import com.M3Tours.empresas.DTO.EmpresaDTO;
import com.M3Tours.empresas.Model.Empresa;
import com.M3Tours.empresas.Service.EmpresaService;



@RestController
@RequestMapping("/api/v1/empresas")
public class EmpresaController {
    @Autowired
    private EmpresaService service;

    @PostMapping("/agregar-empresa")
    public ResponseEntity<String> save(@RequestBody EmpresaDTO empresa) {
        service.save(empresa);
        return ResponseEntity.ok("Empresa añadida con exito.");
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        if (service.delete(id)){
            return ResponseEntity.ok("Empresa eliminada con exito");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa con ID " + id + " No encontrada.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Empresa>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Empresa> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Empresa> getByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(service.findByNombre(nombre));
    }

    @GetMapping("/rut/{rutEmpresa}")
    public ResponseEntity<Empresa> getByRut(@PathVariable String rutEmpresa) {
        return ResponseEntity.ok(service.findByRut(rutEmpresa));
    }
}