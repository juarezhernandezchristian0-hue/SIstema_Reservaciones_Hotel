package com.hotel.crm_hotel.controllers;

import com.hotel.crm_hotel.models.TipoHabitacion;
import com.hotel.crm_hotel.services.TipoHabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/tipos-habitacion")
public class TipoHabitacionController {

    @Autowired
    private TipoHabitacionService tipoHabitacionService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody TipoHabitacion tipoHabitacion) {
        try {
            TipoHabitacion nuevoTipo = tipoHabitacionService.crear(tipoHabitacion);
            return ResponseEntity.ok(nuevoTipo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerById(@PathVariable Long id) {
        Optional<TipoHabitacion> tipo = tipoHabitacionService.obtenerById(id);
        if (tipo.isPresent()) {
            return ResponseEntity.ok(tipo.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<TipoHabitacion>> obtenerTodos() {
        List<TipoHabitacion> tipos = tipoHabitacionService.obtenerTodos();
        return ResponseEntity.ok(tipos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody TipoHabitacion tipoActualizado) {
        try {
            TipoHabitacion tipo = tipoHabitacionService.actualizar(id, tipoActualizado);
            return ResponseEntity.ok(tipo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            tipoHabitacionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}