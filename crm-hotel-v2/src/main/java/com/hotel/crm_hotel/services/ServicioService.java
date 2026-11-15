package com.hotel.crm_hotel.services;

import com.hotel.crm_hotel.models.Servicio;
import com.hotel.crm_hotel.repositories.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    public Servicio crear(Servicio servicio) {
        if (servicio.getNombre() == null || servicio.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del servicio no puede estar vacío");
        }
        if (servicio.getPrecio() == null || servicio.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        return servicioRepository.save(servicio);
    }

    public Optional<Servicio> obtenerById(Long id) {
        return servicioRepository.findById(id);
    }

    public List<Servicio> obtenerTodos() {
        return servicioRepository.findAll();
    }

    public Servicio actualizar(Long id, Servicio servicioActualizado) {
        Optional<Servicio> servicioExistente = servicioRepository.findById(id);
        if (servicioExistente.isPresent()) {
            Servicio servicio = servicioExistente.get();
            if (servicioActualizado.getNombre() != null && !servicioActualizado.getNombre().isEmpty()) {
                servicio.setNombre(servicioActualizado.getNombre());
            }
            if (servicioActualizado.getPrecio() != null && servicioActualizado.getPrecio() > 0) {
                servicio.setPrecio(servicioActualizado.getPrecio());
            }
            if (servicioActualizado.getDescripcion() != null && !servicioActualizado.getDescripcion().isEmpty()) {
                servicio.setDescripcion(servicioActualizado.getDescripcion());
            }
            return servicioRepository.save(servicio);
        }
        throw new IllegalArgumentException("Servicio no encontrado con ID: " + id);
    }

    public void eliminar(Long id) {
        if (servicioRepository.existsById(id)) {
            servicioRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Servicio no encontrado con ID: " + id);
        }
    }
}