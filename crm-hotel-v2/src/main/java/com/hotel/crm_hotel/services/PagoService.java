package com.hotel.crm_hotel.services;

import com.hotel.crm_hotel.models.Pago;
import com.hotel.crm_hotel.repositories.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public Pago crear(Pago pago) {
        if (pago.getReservacion() == null) {
            throw new IllegalArgumentException("La reservación no puede ser nula");
        }
        if (pago.getMonto() == null || pago.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
        if (pago.getMetodoPago() == null || pago.getMetodoPago().isEmpty()) {
            throw new IllegalArgumentException("El método de pago no puede estar vacío");
        }

        pago.setEstado("PAGADO");
        return pagoRepository.save(pago);
    }

    public Optional<Pago> obtenerById(Long id) {
        return pagoRepository.findById(id);
    }

    public List<Pago> obtenerTodos() {
        return pagoRepository.findAll();
    }

    public Pago actualizar(Long id, Pago pagoActualizado) {
        Optional<Pago> pagoExistente = pagoRepository.findById(id);
        if (pagoExistente.isPresent()) {
            Pago pago = pagoExistente.get();
            if (pagoActualizado.getReservacion() != null) {
                pago.setReservacion(pagoActualizado.getReservacion());
            }
            if (pagoActualizado.getMonto() != null && pagoActualizado.getMonto() > 0) {
                pago.setMonto(pagoActualizado.getMonto());
            }
            if (pagoActualizado.getMetodoPago() != null && !pagoActualizado.getMetodoPago().isEmpty()) {
                pago.setMetodoPago(pagoActualizado.getMetodoPago());
            }
            if (pagoActualizado.getEstado() != null && !pagoActualizado.getEstado().isEmpty()) {
                pago.setEstado(pagoActualizado.getEstado());
            }
            return pagoRepository.save(pago);
        }
        throw new IllegalArgumentException("Pago no encontrado con ID: " + id);
    }

    public void eliminar(Long id) {
        if (pagoRepository.existsById(id)) {
            pagoRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Pago no encontrado con ID: " + id);
        }
    }
}