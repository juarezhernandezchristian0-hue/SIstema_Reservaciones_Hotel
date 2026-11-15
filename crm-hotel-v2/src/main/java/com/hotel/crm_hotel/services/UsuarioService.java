package com.hotel.crm_hotel.services;

import com.hotel.crm_hotel.models.Usuario;
import com.hotel.crm_hotel.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private static final List<String> ROLES = List.of("ADMIN", "RECEPCIONISTA");

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario crear(Usuario usuario) {
        validar(usuario, true, null);
        usuario.setId(null);
        usuario.setUsername(usuario.getUsername().trim());
        usuario.setEmail(usuario.getEmail().trim());
        usuario.setRol(usuario.getRol().trim().toUpperCase());
        usuario.setEstado(vacio(usuario.getEstado()) ? "ACTIVO" : usuario.getEstado().trim().toUpperCase());
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> obtenerById(Long id) {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario actualizar(Long id, Usuario datos) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));
        validar(datos, false, id);
        u.setUsername(datos.getUsername().trim());
        u.setEmail(datos.getEmail().trim());
        u.setRol(datos.getRol().trim().toUpperCase());
        if (!vacio(datos.getEstado())) u.setEstado(datos.getEstado().trim().toUpperCase());
        if (!vacio(datos.getPassword())) u.setPassword(passwordEncoder.encode(datos.getPassword()));
        return usuarioRepository.save(u);
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));
        if ("sistema_web".equals(u.getUsername())) {
            throw new IllegalArgumentException("Este usuario es del sistema y no se puede eliminar");
        }
        try {
            usuarioRepository.delete(u);
            usuarioRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("No se puede eliminar: el usuario tiene reservaciones registradas");
        }
    }

    private void validar(Usuario u, boolean esNuevo, Long idActual) {
        if (vacio(u.getUsername())) throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        if (esNuevo && vacio(u.getPassword())) throw new IllegalArgumentException("La contraseña es obligatoria");
        if (!vacio(u.getPassword()) && u.getPassword().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        if (vacio(u.getEmail())) throw new IllegalArgumentException("El correo es obligatorio");
        if (vacio(u.getRol()) || !ROLES.contains(u.getRol().trim().toUpperCase())) {
            throw new IllegalArgumentException("El rol debe ser ADMIN o RECEPCIONISTA");
        }
        Optional<Usuario> existente = usuarioRepository.findByUsername(u.getUsername().trim());
        if (existente.isPresent() && !existente.get().getId().equals(idActual)) {
            throw new IllegalArgumentException("Ese nombre de usuario ya existe");
        }
    }

    private boolean vacio(String s) {
        return s == null || s.isBlank();
    }
}
