package com.hotel.crm_hotel.controllers;

import com.hotel.crm_hotel.models.DetalleReservacion;
import com.hotel.crm_hotel.services.DetalleReservacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/detalles-reservacion")
public class DetalleReservacionController {

    @Autowired
    private DetalleReservacionService detalleReservacionService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody DetalleReservacion detalleReservacion) {
        try {
            DetalleReservacion nuevoDetalle = detalleReservacionService.crear(detalleReservacion);
            return ResponseEntity.ok(nuevoDetalle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerById(@PathVariable Long id) {
        Optional<DetalleReservacion> detalle = detalleReservacionService.obtenerById(id);
        if (detalle.isPresent()) {
            return ResponseEntity.ok(detalle.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<DetalleReservacion>> obtenerTodos() {
        List<DetalleReservacion> detalles = detalleReservacionService.obtenerTodos();
        return ResponseEntity.ok(detalles);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody DetalleReservacion detalleActualizado) {
        try {
            DetalleReservacion detalle = detalleReservacionService.actualizar(id, detalleActualizado);
            return ResponseEntity.ok(detalle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            detalleReservacionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}