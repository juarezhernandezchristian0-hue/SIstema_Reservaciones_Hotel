package com.hotel.crm_hotel.models;

import jakarta.persistence.*;

// El precio ya no vive aquí: se toma del tipo de habitación (precio_noche)
@Entity
@Table(name = "habitaciones")
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String numero;

    @ManyToOne
    @JoinColumn(name = "tipo_habitacion_id", nullable = false)
    private TipoHabitacion tipoHabitacion;

    public Habitacion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public TipoHabitacion getTipoHabitacion() { return tipoHabitacion; }
    public void setTipoHabitacion(TipoHabitacion tipoHabitacion) { this.tipoHabitacion = tipoHabitacion; }
}
