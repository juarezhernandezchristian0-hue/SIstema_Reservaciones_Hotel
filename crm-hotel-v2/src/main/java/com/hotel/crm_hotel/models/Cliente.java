package com.hotel.crm_hotel.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    public Cliente() {}

    public Cliente(Long id, String nombre, String telefono, String email, LocalDateTime fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.fechaRegistro = fechaRegistro;
    }

    public Cliente(String nombre, String telefono, String email) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.fechaRegistro = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEmail(String email) { this.email = email; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    @Override
    public String toString() {
        return "Cliente{" + "id=" + id + ", nombre='" + nombre + '\'' + ", telefono='" + telefono + '\'' + ", email='" + email + '\'' + ", fechaRegistro=" + fechaRegistro + '}';
    }
}