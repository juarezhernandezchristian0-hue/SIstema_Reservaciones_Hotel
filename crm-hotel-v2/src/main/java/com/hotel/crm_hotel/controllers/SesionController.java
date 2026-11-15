package com.hotel.crm_hotel.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

// Dice al panel quien esta conectado y que rol tiene (para mostrar u ocultar opciones)
@RestController
@RequestMapping("/api/sesion")
public class SesionController {

    @GetMapping
    public Map<String, String> sesion(Authentication auth) {
        String rol = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        return Map.of("usuario", auth.getName(), "rol", rol);
    }
}
