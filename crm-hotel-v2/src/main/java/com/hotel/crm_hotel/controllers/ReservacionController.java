package com.hotel.crm_hotel.controllers;

import com.hotel.crm_hotel.dto.ReservaRequest;
import com.hotel.crm_hotel.models.Reservacion;
import com.hotel.crm_hotel.services.ReservacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.hotel.crm_hotel.dto.ConfirmacionReserva;
import com.hotel.crm_hotel.services.EmailService;

@RestController
@RequestMapping("/api/reservaciones")
public class ReservacionController {

    @Autowired
    private ReservacionService reservacionService;

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<?> crearDesdeWeb(@RequestBody ReservaRequest request) {
        try {
            ConfirmacionReserva confirmacion = reservacionService.crearDesdeWeb(request);
            // La reservacion ya quedo guardada; si el correo falla, solo se registra en el log
            emailService.enviarConfirmacion(request.nombre(), request.email(), confirmacion);
            return ResponseEntity.ok(confirmacion);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ADMIN o RECEPCIONISTA
    @GetMapping
    public ResponseEntity<List<Reservacion>> obtenerTodas() {
        return ResponseEntity.ok(reservacionService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerById(@PathVariable("id") Long id) {
        Optional<Reservacion> reservacion = reservacionService.obtenerById(id);
        if (reservacion.isPresent()) return ResponseEntity.ok(reservacion.get());
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") Long id, @RequestBody Reservacion datos) {
        try {
            return ResponseEntity.ok(reservacionService.actualizar(id, datos));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarReservacion(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(reservacionService.cancelarReservacion(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // SOLO ADMIN (regla en SecurityConfig)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable("id") Long id) {
        try {
            reservacionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
