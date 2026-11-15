package com.hotel.crm_hotel.services;

import com.hotel.crm_hotel.models.Resena;
import com.hotel.crm_hotel.repositories.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    public List<Resena> obtenerTodas() {
        return resenaRepository.findAllByOrderByFechaDesc();
    }

    public Resena crear(Resena resena) {
        if (vacio(resena.getNombreCliente())) throw new IllegalArgumentException("Escribe tu nombre");
        if (resena.getNombreCliente().trim().length() > 100) throw new IllegalArgumentException("El nombre es demasiado largo");
        if (vacio(resena.getComentario())) throw new IllegalArgumentException("Escribe tu opinión");
        if (resena.getComentario().trim().length() > 1000) throw new IllegalArgumentException("La opinión no puede pasar de 1000 caracteres");
        if (resena.getCalificacion() == null || resena.getCalificacion() < 1 || resena.getCalificacion() > 5) {
            throw new IllegalArgumentException("La calificación debe ser de 1 a 5 estrellas");
        }
        resena.setId(null);
        resena.setNombreCliente(resena.getNombreCliente().trim());
        resena.setComentario(resena.getComentario().trim());
        resena.setFecha(LocalDateTime.now());
        return resenaRepository.save(resena);
    }

    public void eliminar(Long id) {
        if (!resenaRepository.existsById(id)) throw new IllegalArgumentException("Reseña no encontrada con ID: " + id);
        resenaRepository.deleteById(id);
    }

    private boolean vacio(String s) {
        return s == null || s.isBlank();
    }
}
