package com.hotel.crm_hotel.models;

import jakarta.persistence.*;

@Entity
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private Double precio;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    public Servicio() {}
    public Servicio(Long id, String nombre, Double precio, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
    }
    public Servicio(String nombre, Double precio, String descripcion) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Double getPrecio() { return precio; }
    public String getDescripcion() { return descripcion; }
    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return "Servicio{" + "id=" + id + ", nombre='" + nombre + '\'' + ", precio=" + precio + ", descripcion='" + descripcion + '\'' + '}';
    }
}