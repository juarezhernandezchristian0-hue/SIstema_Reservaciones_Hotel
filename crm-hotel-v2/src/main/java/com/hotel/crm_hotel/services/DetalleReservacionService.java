package com.hotel.crm_hotel.services;

import com.hotel.crm_hotel.models.DetalleReservacion;
import com.hotel.crm_hotel.repositories.DetalleReservacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DetalleReservacionService {

    @Autowired
    private DetalleReservacionRepository detalleReservacionRepository;

    public DetalleReservacion crear(DetalleReservacion detalleReservacion) {
        if (detalleReservacion.getReservacion() == null) {
            throw new IllegalArgumentException("La reservación no puede ser nula");
        }
        if (detalleReservacion.getServicio() == null) {
            throw new IllegalArgumentException("El servicio no puede ser nulo");
        }
        if (detalleReservacion.getCantidad() == null || detalleReservacion.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        detalleReservacion.setPrecioServicio(detalleReservacion.getServicio().getPrecio());
        return detalleReservacionRepository.save(detalleReservacion);
    }

    public Optional<DetalleReservacion> obtenerById(Long id) {
        return detalleReservacionRepository.findById(id);
    }

    public List<DetalleReservacion> obtenerTodos() {
        return detalleReservacionRepository.findAll();
    }

    public DetalleReservacion actualizar(Long id, DetalleReservacion detalleActualizado) {
        Optional<DetalleReservacion> detalleExistente = detalleReservacionRepository.findById(id);
        if (detalleExistente.isPresent()) {
            DetalleReservacion detalle = detalleExistente.get();
            if (detalleActualizado.getReservacion() != null) {
                detalle.setReservacion(detalleActualizado.getReservacion());
            }
            if (detalleActualizado.getServicio() != null) {
                detalle.setServicio(detalleActualizado.getServicio());
                detalle.setPrecioServicio(detalleActualizado.getServicio().getPrecio());
            }
            if (detalleActualizado.getCantidad() != null && detalleActualizado.getCantidad() > 0) {
                detalle.setCantidad(detalleActualizado.getCantidad());
            }
            return detalleReservacionRepository.save(detalle);
        }
        throw new IllegalArgumentException("Detalle de reservación no encontrado con ID: " + id);
    }

    public void eliminar(Long id) {
        if (detalleReservacionRepository.existsById(id)) {
            detalleReservacionRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Detalle de reservación no encontrado con ID: " + id);
        }
    }
}