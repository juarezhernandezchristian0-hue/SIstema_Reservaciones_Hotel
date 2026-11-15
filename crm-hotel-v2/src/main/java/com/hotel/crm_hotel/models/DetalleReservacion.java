package com.hotel.crm_hotel.models;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_reservaciones")
public class DetalleReservacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservacion_id", nullable = false)
    private Reservacion reservacion;

    @ManyToOne
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precioServicio;

    public DetalleReservacion() {}
    public DetalleReservacion(Long id, Reservacion reservacion, Servicio servicio, Integer cantidad, Double precioServicio) {
        this.id = id;
        this.reservacion = reservacion;
        this.servicio = servicio;
        this.cantidad = cantidad;
        this.precioServicio = precioServicio;
    }
    public DetalleReservacion(Reservacion reservacion, Servicio servicio, Integer cantidad, Double precioServicio) {
        this.reservacion = reservacion;
        this.servicio = servicio;
        this.cantidad = cantidad;
        this.precioServicio = precioServicio;
    }

    public Long getId() { return id; }
    public Reservacion getReservacion() { return reservacion; }
    public Servicio getServicio() { return servicio; }
    public Integer getCantidad() { return cantidad; }
    public Double getPrecioServicio() { return precioServicio; }
    public void setId(Long id) { this.id = id; }
    public void setReservacion(Reservacion reservacion) { this.reservacion = reservacion; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public void setPrecioServicio(Double precioServicio) { this.precioServicio = precioServicio; }

    @Override
    public String toString() {
        return "DetalleReservacion{" + "id=" + id + ", reservacion=" + reservacion + ", servicio=" + servicio + ", cantidad=" + cantidad + ", precioServicio=" + precioServicio + '}';
    }
}