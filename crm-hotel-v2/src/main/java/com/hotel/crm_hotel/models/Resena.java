package com.hotel.crm_hotel.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resenas")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_cliente", nullable = false, length = 100)
    private String nombreCliente;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comentario;

    @Column(nullable = false)
    private Integer calificacion;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha;

    public Resena() {}

    // Regla del hotel: 1 estrella = Mala, 5 = Buena, 2 a 4 = Regular
    public String getClasificacion() {
        if (calificacion == null) return "SIN CALIFICAR";
        if (calificacion == 1) return "MALA";
        if (calificacion == 5) return "BUENA";
        return "REGULAR";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
