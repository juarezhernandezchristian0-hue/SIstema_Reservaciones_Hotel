package com.hotel.crm_hotel.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF se desactiva solo para la API, el login y el logout (formularios simples)
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", "/login", "/logout"))
            .authorizeHttpRequests(auth -> auth
                // Paginas publicas
                .requestMatchers("/", "/index.html", "/login.html", "/css/**", "/js/**", "/img/**", "/favicon.ico", "/error").permitAll()
                // Consultas publicas: tipos, habitaciones, disponibilidad y opiniones
                .requestMatchers(HttpMethod.GET, "/api/tipos-habitacion/**", "/api/habitaciones", "/api/habitaciones/disponibilidad", "/api/resenas").permitAll()
                // Acciones publicas del cliente: reservar y opinar
                .requestMatchers(HttpMethod.POST, "/api/reservaciones", "/api/resenas").permitAll()
                // Panel del personal
                .requestMatchers("/admin.html", "/api/sesion").hasAnyRole("ADMIN", "RECEPCIONISTA")
                // Solo ADMIN: usuarios, habitaciones, tipos y servicios
                .requestMatchers("/api/usuarios/**", "/api/tipos-habitacion/**", "/api/habitaciones/**", "/api/servicios/**").hasRole("ADMIN")
                // Solo ADMIN puede eliminar cualquier registro
                .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                // Resto de la API (reservaciones, clientes, pagos, detalles): ADMIN o RECEPCIONISTA
                .requestMatchers("/api/**").hasAnyRole("ADMIN", "RECEPCIONISTA")
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/admin.html", true)
                .failureUrl("/login.html?error")
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html?logout")
                .permitAll())
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    // API: 401 (el JavaScript lo maneja). Paginas: redirigir al login.
                    if (request.getRequestURI().startsWith("/api/")) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    } else {
                        response.sendRedirect("/login.html");
                    }
                }));
        return http.build();
    }
}
