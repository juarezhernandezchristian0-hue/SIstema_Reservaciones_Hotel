package com.hotel.crm_hotel.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservacion_id", nullable = false)
    private Reservacion reservacion;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private String metodoPago;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private String estado;

    public Pago() {}
    public Pago(Long id, Reservacion reservacion, Double monto, String metodoPago, LocalDateTime fecha, String estado) {
        this.id = id;
        this.reservacion = reservacion;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.fecha = fecha;
        this.estado = estado;
    }
    public Pago(Reservacion reservacion, Double monto, String metodoPago, String estado) {
        this.reservacion = reservacion;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.fecha = LocalDateTime.now();
        this.estado = estado;
    }

    public Long getId() { return id; }
    public Reservacion getReservacion() { return reservacion; }
    public Double getMonto() { return monto; }
    public String getMetodoPago() { return metodoPago; }
    public LocalDateTime getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public void setId(Long id) { this.id = id; }
    public void setReservacion(Reservacion reservacion) { this.reservacion = reservacion; }
    public void setMonto(Double monto) { this.monto = monto; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Pago{" + "id=" + id + ", reservacion=" + reservacion + ", monto=" + monto + ", metodoPago='" + metodoPago + '\'' + ", fecha=" + fecha + ", estado='" + estado + '\'' + '}';
    }
}