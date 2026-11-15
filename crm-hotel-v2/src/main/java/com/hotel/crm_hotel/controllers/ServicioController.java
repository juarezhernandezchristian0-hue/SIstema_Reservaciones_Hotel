package com.hotel.crm_hotel.controllers;

import com.hotel.crm_hotel.models.Servicio;
import com.hotel.crm_hotel.services.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Servicio servicio) {
        try {
            Servicio nuevoServicio = servicioService.crear(servicio);
            return ResponseEntity.ok(nuevoServicio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerById(@PathVariable Long id) {
        Optional<Servicio> servicio = servicioService.obtenerById(id);
        if (servicio.isPresent()) {
            return ResponseEntity.ok(servicio.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Servicio>> obtenerTodos() {
        List<Servicio> servicios = servicioService.obtenerTodos();
        return ResponseEntity.ok(servicios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Servicio servicioActualizado) {
        try {
            Servicio servicio = servicioService.actualizar(id, servicioActualizado);
            return ResponseEntity.ok(servicio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            servicioService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}