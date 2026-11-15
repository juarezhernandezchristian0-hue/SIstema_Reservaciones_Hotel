package com.hotel.crm_hotel.dto;

import java.time.LocalDate;

// Lo que envía el cliente desde la página pública (nunca pide IDs de cliente ni de usuario)
public record ReservaRequest(
        String nombre,
        String email,
        String telefono,
        Long tipoHabitacionId,
        String numeroHabitacion,
        LocalDate fechaEntrada,
        LocalDate fechaSalida,
        Integer numeroHuespedes,
        String metodoPago
) {}
