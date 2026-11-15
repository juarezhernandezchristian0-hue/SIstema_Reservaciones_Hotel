package com.hotel.crm_hotel.services;

import com.hotel.crm_hotel.dto.DisponibilidadHabitacion;
import com.hotel.crm_hotel.models.Habitacion;
import com.hotel.crm_hotel.models.TipoHabitacion;
import com.hotel.crm_hotel.repositories.HabitacionRepository;
import com.hotel.crm_hotel.repositories.ReservacionRepository;
import com.hotel.crm_hotel.repositories.TipoHabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class HabitacionService {

    @Autowired
    private HabitacionRepository habitacionRepository;
    @Autowired
    private TipoHabitacionRepository tipoHabitacionRepository;
    @Autowired
    private ReservacionRepository reservacionRepository;

    // Publico: numero y estado de cada habitacion del tipo, sin IDs
    public List<DisponibilidadHabitacion> disponibilidad(Long tipoId, LocalDate entrada, LocalDate salida) {
        if (tipoId == null || entrada == null || salida == null) {
            throw new IllegalArgumentException("Faltan datos para consultar disponibilidad");
        }
        if (!salida.isAfter(entrada)) throw new IllegalArgumentException("La salida debe ser posterior a la entrada");
        return habitacionRepository.findByTipoHabitacionId(tipoId).stream()
                .sorted(Comparator.comparingInt(this::numeroComoEntero))
                .map(h -> new DisponibilidadHabitacion(h.getNumero(),
                        reservacionRepository.findSolapadas(h.getId(), ReservacionService.CONFIRMADA, entrada, salida).isEmpty()))
                .toList();
    }

    public Habitacion crear(Habitacion habitacion) {
        validarNumero(habitacion.getNumero(), null);
        habitacion.setId(null);
        habitacion.setNumero(habitacion.getNumero().trim());
        habitacion.setTipoHabitacion(obtenerTipo(habitacion));
        return habitacionRepository.save(habitacion);
    }

    public Optional<Habitacion> obtenerById(Long id) {
        return habitacionRepository.findById(id);
    }

    public List<Habitacion> obtenerTodos() {
        return habitacionRepository.findAll().stream()
                .sorted(Comparator.comparingInt(this::numeroComoEntero))
                .toList();
    }

    public Habitacion actualizar(Long id, Habitacion datos) {
        Habitacion h = habitacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Habitación no encontrada con ID: " + id));
        validarNumero(datos.getNumero(), id);
        h.setNumero(datos.getNumero().trim());
        h.setTipoHabitacion(obtenerTipo(datos));
        return habitacionRepository.save(h);
    }

    public void eliminar(Long id) {
        if (!habitacionRepository.existsById(id)) {
            throw new IllegalArgumentException("Habitación no encontrada con ID: " + id);
        }
        if (reservacionRepository.existsByHabitacionId(id)) {
            throw new IllegalArgumentException("No se puede eliminar: la habitación tiene reservaciones registradas");
        }
        habitacionRepository.deleteById(id);
    }

    private void validarNumero(String numero, Long idActual) {
        if (numero == null || numero.isBlank()) throw new IllegalArgumentException("El número de habitación es obligatorio");
        boolean repetido = habitacionRepository.findByNumero(numero.trim())
                .filter(h -> !h.getId().equals(idActual))
                .isPresent();
        if (repetido) throw new IllegalArgumentException("Ya existe una habitación con el número " + numero.trim());
    }

    private TipoHabitacion obtenerTipo(Habitacion h) {
        if (h.getTipoHabitacion() == null || h.getTipoHabitacion().getId() == null) {
            throw new IllegalArgumentException("El tipo de habitación es obligatorio");
        }
        return tipoHabitacionRepository.findById(h.getTipoHabitacion().getId())
                .orElseThrow(() -> new IllegalArgumentException("El tipo de habitación no existe"));
    }

    private int numeroComoEntero(Habitacion h) {
        if (h.getNumero() == null) return Integer.MAX_VALUE;
        try {
            return Integer.parseInt(h.getNumero().trim());
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }
}
