package com.hotel.crm_hotel.dto;

// Solo número y estado: el cliente nunca ve IDs internos
public record DisponibilidadHabitacion(String numero, boolean disponible) {}
