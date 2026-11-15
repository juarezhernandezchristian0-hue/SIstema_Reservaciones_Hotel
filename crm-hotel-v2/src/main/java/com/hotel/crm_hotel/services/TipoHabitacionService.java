package com.hotel.crm_hotel.services;

import com.hotel.crm_hotel.models.TipoHabitacion;
import com.hotel.crm_hotel.repositories.HabitacionRepository;
import com.hotel.crm_hotel.repositories.TipoHabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TipoHabitacionService {

    @Autowired
    private TipoHabitacionRepository tipoHabitacionRepository;
    @Autowired
    private HabitacionRepository habitacionRepository;

    public TipoHabitacion crear(TipoHabitacion tipo) {
        validar(tipo, null);
        tipo.setId(null);
        tipo.setNombre(tipo.getNombre().trim());
        tipo.setTamano(vacio(tipo.getTamano()) ? null : tipo.getTamano().trim());
        tipo.setTipoCama(vacio(tipo.getTipoCama()) ? null : tipo.getTipoCama().trim());
        return tipoHabitacionRepository.save(tipo);
    }

    public Optional<TipoHabitacion> obtenerById(Long id) {
        return tipoHabitacionRepository.findById(id);
    }

    public List<TipoHabitacion> obtenerTodos() {
        return tipoHabitacionRepository.findAll();
    }

    // Reemplaza todos los campos (el admin siempre envia el objeto completo)
    public TipoHabitacion actualizar(Long id, TipoHabitacion datos) {
        TipoHabitacion t = tipoHabitacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de habitación no encontrado con ID: " + id));
        validar(datos, id);
        t.setNombre(datos.getNombre().trim());
        t.setDescripcion(datos.getDescripcion());
        t.setPrecioNoche(datos.getPrecioNoche());
        t.setCapacidadMaxima(datos.getCapacidadMaxima());
        t.setTamano(vacio(datos.getTamano()) ? null : datos.getTamano().trim());
        t.setTipoCama(vacio(datos.getTipoCama()) ? null : datos.getTipoCama().trim());
        t.setTieneWifi(datos.isTieneWifi());
        t.setTieneComida(datos.isTieneComida());
        t.setTieneClima(datos.isTieneClima());
        t.setTieneBano(datos.isTieneBano());
        t.setTieneTv(datos.isTieneTv());
        t.setAmueblada(datos.isAmueblada());
        return tipoHabitacionRepository.save(t);
    }

    public void eliminar(Long id) {
        if (!tipoHabitacionRepository.existsById(id)) {
            throw new IllegalArgumentException("Tipo de habitación no encontrado con ID: " + id);
        }
        if (!habitacionRepository.findByTipoHabitacionId(id).isEmpty()) {
            throw new IllegalArgumentException("No se puede eliminar: hay habitaciones asignadas a este tipo");
        }
        tipoHabitacionRepository.deleteById(id);
    }

    private void validar(TipoHabitacion t, Long idActual) {
        if (vacio(t.getNombre())) throw new IllegalArgumentException("El nombre del tipo es obligatorio");
        if (t.getPrecioNoche() == null || t.getPrecioNoche() <= 0) {
            throw new IllegalArgumentException("El precio por noche debe ser mayor a 0");
        }
        if (t.getCapacidadMaxima() == null || t.getCapacidadMaxima() < 1) {
            throw new IllegalArgumentException("La capacidad máxima debe ser de al menos 1 persona");
        }
        String nombre = t.getNombre().trim();
        boolean repetido = tipoHabitacionRepository.findAll().stream()
                .anyMatch(x -> x.getNombre().equalsIgnoreCase(nombre) && !x.getId().equals(idActual));
        if (repetido) throw new IllegalArgumentException("Ya existe un tipo de habitación con el nombre " + nombre);
    }

    private boolean vacio(String s) {
        return s == null || s.isBlank();
    }
}
