package com.M3Tours.pago.Controller;

import java.util.List;
import java.util.Optional;

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

import com.M3Tours.pago.DTO.PagoDTO;
import com.M3Tours.pago.Model.Pago;
import com.M3Tours.pago.Service.PagoService;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {
    @Autowired
    private PagoService service;

    @GetMapping("")
    public ResponseEntity<List<Pago>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Optional<Pago> pago = service.findById(id);
        
        if (pago.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pago con id '" + id + "' no encontrado");
        }
        return ResponseEntity.ok(pago.get());
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> getByUserId(@PathVariable Integer id) {
        Optional<Pago> pago = service.findByUserId(id);

        if (pago.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pago de usuario con id '" + id + "' no encontrado");
        }
        return ResponseEntity.ok(pago.get());
    }

    @GetMapping("/reserva/{id}")
    public ResponseEntity<?> getByReservaId(@PathVariable Integer id) {
        Optional<Pago> pago = service.findByReservaId(id);

        if (pago.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pago de reserva con id '" + id + "' no encontrado");
        }
        return ResponseEntity.ok(pago.get());
    }

    @PostMapping("")
    public ResponseEntity<String> save(@RequestBody PagoDTO pago) {
        Boolean save = service.save(pago);
        if(save!=true){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body("Error: Usuario o Reserva correspondientes no existen.");
        }
        return ResponseEntity.ok("Pago ejecutado con exito!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Pago eliminado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pago con id '" + id + "' no encontrado");
    }
}
