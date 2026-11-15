package com.hotel.crm_hotel.controllers;

import com.hotel.crm_hotel.models.Pago;
import com.hotel.crm_hotel.services.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Pago pago) {
        try {
            Pago nuevoPago = pagoService.crear(pago);
            return ResponseEntity.ok(nuevoPago);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerById(@PathVariable Long id) {
        Optional<Pago> pago = pagoService.obtenerById(id);
        if (pago.isPresent()) {
            return ResponseEntity.ok(pago.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Pago>> obtenerTodos() {
        List<Pago> pagos = pagoService.obtenerTodos();
        return ResponseEntity.ok(pagos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Pago pagoActualizado) {
        try {
            Pago pago = pagoService.actualizar(id, pagoActualizado);
            return ResponseEntity.ok(pago);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            pagoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}