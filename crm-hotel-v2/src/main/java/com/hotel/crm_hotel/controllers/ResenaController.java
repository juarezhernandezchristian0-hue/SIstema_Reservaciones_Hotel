package com.hotel.crm_hotel.controllers;

import com.hotel.crm_hotel.models.Resena;
import com.hotel.crm_hotel.services.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    // PUBLICO: ver opiniones
    @GetMapping
    public ResponseEntity<List<Resena>> listar() {
        return ResponseEntity.ok(resenaService.obtenerTodas());
    }

    // PUBLICO: dejar una opinion
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Resena resena) {
        try {
            return ResponseEntity.ok(resenaService.crear(resena));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // SOLO ADMIN (regla en SecurityConfig)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable("id") Long id) {
        try {
            resenaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
