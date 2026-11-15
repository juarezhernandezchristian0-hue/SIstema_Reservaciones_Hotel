package com.hotel.crm_hotel.services;

import com.hotel.crm_hotel.models.Cliente;
import com.hotel.crm_hotel.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente crear(Cliente cliente) {
        if (cliente.getNombre() == null || cliente.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío");
        }
        if (cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
            throw new IllegalArgumentException("El email del cliente no puede estar vacío");
        }

        // ✅ NUEVO: Asignar fechaRegistro si es null
        if (cliente.getFechaRegistro() == null) {
            cliente.setFechaRegistro(LocalDateTime.now());
        }

        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> obtenerById(Long id) {
        return clienteRepository.findById(id);
    }

    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    public Cliente actualizar(Long id, Cliente clienteActualizado) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            if (clienteActualizado.getNombre() != null && !clienteActualizado.getNombre().isEmpty()) {
                cliente.setNombre(clienteActualizado.getNombre());
            }
            if (clienteActualizado.getTelefono() != null && !clienteActualizado.getTelefono().isEmpty()) {
                cliente.setTelefono(clienteActualizado.getTelefono());
            }
            if (clienteActualizado.getEmail() != null && !clienteActualizado.getEmail().isEmpty()) {
                cliente.setEmail(clienteActualizado.getEmail());
            }
            return clienteRepository.save(cliente);
        }
        throw new IllegalArgumentException("Cliente no encontrado con ID: " + id);
    }

    public void eliminar(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Cliente no encontrado con ID: " + id);
        }
    }
}