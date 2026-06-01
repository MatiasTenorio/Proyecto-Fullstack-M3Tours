package com.M3Tours.detallepago.Controller;

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

import com.M3Tours.detallepago.DTO.DetallePagoDTO;
import com.M3Tours.detallepago.Model.DetallePago;
import com.M3Tours.detallepago.Service.DetallePagoService;

@RestController
@RequestMapping("/api/v1/detalle-pagos")
public class DetallePagoController {
    @Autowired
    private DetallePagoService service;

    @GetMapping("")
    public ResponseEntity<List<DetallePago>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallePago> getById(@PathVariable Integer id) {
        Optional<DetallePago> detallePago = service.findById(id);
        if (detallePago.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(detallePago.get());
    }

    @GetMapping("/boleta/{numeroBoleta}")
    public ResponseEntity<DetallePago> getByNumeroBoleta(@PathVariable String numeroBoleta) {
        Optional<DetallePago> detallePago = service.findByNumeroBoleta(numeroBoleta);
        if (detallePago.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(detallePago.get());
    }

    @GetMapping("/tipo-pago/{tipoPago}")
    public ResponseEntity<DetallePago> getByTipoPago(@PathVariable String tipoPago) {
        Optional<DetallePago> detallePago = service.findByTipoPago(tipoPago);
        if (detallePago.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(detallePago.get());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<DetallePago> getByEstado(@PathVariable String estado) {
        Optional<DetallePago> detallePago = service.findByEstado(estado);
        if (detallePago.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(detallePago.get());
    }

    @GetMapping("/tour/{nombreTour}")
    public ResponseEntity<DetallePago> getByNombreTour(@PathVariable String nombreTour) {
        Optional<DetallePago> detallePago = service.findByNombreTour(nombreTour);
        if (detallePago.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(detallePago.get());
    }

    @PostMapping("")
    public ResponseEntity<String> save(@RequestBody DetallePagoDTO detallePago) {
        Boolean save = service.save(detallePago);
        if (save != true) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Pago correspondiente no existe.");
        }
        return ResponseEntity.ok("Detalle de pago guardado con exito!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        Boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Detalle de pago eliminado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Detalle de pago con id '" + id + "' no encontrado");
    }

}