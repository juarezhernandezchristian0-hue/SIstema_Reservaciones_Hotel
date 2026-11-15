package com.hotel.crm_hotel.controllers;

import com.hotel.crm_hotel.models.Habitacion;
import com.hotel.crm_hotel.services.HabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/habitaciones")
public class HabitacionController {

    @Autowired
    private HabitacionService habitacionService;

    // PUBLICO: disponibilidad por tipo y fechas
    @GetMapping("/disponibilidad")
    public ResponseEntity<?> disponibilidad(
            @RequestParam("tipoId") Long tipoId,
            @RequestParam("entrada") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate entrada,
            @RequestParam("salida") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate salida) {
        try {
            return ResponseEntity.ok(habitacionService.disponibilidad(tipoId, entrada, salida));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // PUBLICO
    @GetMapping
    public ResponseEntity<List<Habitacion>> obtenerTodas() {
        return ResponseEntity.ok(habitacionService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerById(@PathVariable("id") Long id) {
        Optional<Habitacion> habitacion = habitacionService.obtenerById(id);
        if (habitacion.isPresent()) return ResponseEntity.ok(habitacion.get());
        return ResponseEntity.notFound().build();
    }

    // SOLO ADMIN (regla en SecurityConfig)
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Habitacion habitacion) {
        try {
            return ResponseEntity.ok(habitacionService.crear(habitacion));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") Long id, @RequestBody Habitacion habitacionActualizada) {
        try {
            return ResponseEntity.ok(habitacionService.actualizar(id, habitacionActualizada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable("id") Long id) {
        try {
            habitacionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
