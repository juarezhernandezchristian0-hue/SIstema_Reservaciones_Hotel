package com.hotel.crm_hotel.dto;

import java.time.LocalDate;

public record ConfirmacionReserva(
        String habitacion,
        String tipoHabitacion,
        LocalDate fechaEntrada,
        LocalDate fechaSalida,
        int noches,
        double total,
        String metodoPago,
        String estado
) {}
