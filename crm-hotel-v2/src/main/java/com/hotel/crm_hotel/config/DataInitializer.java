package com.hotel.crm_hotel.config;

import com.hotel.crm_hotel.models.Usuario;
import com.hotel.crm_hotel.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 1) Cifra las contrasenas que aun estan en texto plano (solo cambia las que no son BCrypt)
        for (Usuario usuario : usuarioRepository.findAll()) {
            String password = usuario.getPassword();
            if (password != null && !password.startsWith("$2")) {
                usuario.setPassword(passwordEncoder.encode(password));
                usuarioRepository.save(usuario);
            }
        }
        // 2) Usuario interno: las reservaciones hechas desde la web quedan a su nombre (no puede iniciar sesion)
        if (usuarioRepository.findByUsername("sistema_web").isEmpty()) {
            usuarioRepository.save(new Usuario("sistema_web",
                    passwordEncoder.encode(UUID.randomUUID().toString()),
                    "web@hotel.local", "SISTEMA", "INACTIVO"));
        }
    }
}
