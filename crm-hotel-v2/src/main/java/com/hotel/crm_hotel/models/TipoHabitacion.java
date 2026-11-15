package com.hotel.crm_hotel.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_habitaciones")
public class TipoHabitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "precio_noche", nullable = false)
    private Double precioNoche;

    @Column(name = "capacidad_maxima", nullable = false)
    private Integer capacidadMaxima;

    @Column(length = 50)
    private String tamano;

    @Column(name = "tipo_cama", length = 100)
    private String tipoCama;

    @Column(name = "tiene_wifi", nullable = false)
    private boolean tieneWifi;

    @Column(name = "tiene_comida", nullable = false)
    private boolean tieneComida;

    @Column(name = "tiene_clima", nullable = false)
    private boolean tieneClima;

    @Column(name = "tiene_bano", nullable = false)
    private boolean tieneBano;

    @Column(name = "tiene_tv", nullable = false)
    private boolean tieneTv;

    @Column(nullable = false)
    private boolean amueblada;

    public TipoHabitacion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Double getPrecioNoche() { return precioNoche; }
    public void setPrecioNoche(Double precioNoche) { this.precioNoche = precioNoche; }
    public Integer getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(Integer capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }
    public String getTamano() { return tamano; }
    public void setTamano(String tamano) { this.tamano = tamano; }
    public String getTipoCama() { return tipoCama; }
    public void setTipoCama(String tipoCama) { this.tipoCama = tipoCama; }
    public boolean isTieneWifi() { return tieneWifi; }
    public void setTieneWifi(boolean tieneWifi) { this.tieneWifi = tieneWifi; }
    public boolean isTieneComida() { return tieneComida; }
    public void setTieneComida(boolean tieneComida) { this.tieneComida = tieneComida; }
    public boolean isTieneClima() { return tieneClima; }
    public void setTieneClima(boolean tieneClima) { this.tieneClima = tieneClima; }
    public boolean isTieneBano() { return tieneBano; }
    public void setTieneBano(boolean tieneBano) { this.tieneBano = tieneBano; }
    public boolean isTieneTv() { return tieneTv; }
    public void setTieneTv(boolean tieneTv) { this.tieneTv = tieneTv; }
    public boolean isAmueblada() { return amueblada; }
    public void setAmueblada(boolean amueblada) { this.amueblada = amueblada; }
}
