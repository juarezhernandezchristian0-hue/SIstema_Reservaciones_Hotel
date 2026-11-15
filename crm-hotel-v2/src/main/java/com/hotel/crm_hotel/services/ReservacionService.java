package com.hotel.crm_hotel.services;

import com.hotel.crm_hotel.dto.ConfirmacionReserva;
import com.hotel.crm_hotel.dto.ReservaRequest;
import com.hotel.crm_hotel.models.Cliente;
import com.hotel.crm_hotel.models.Habitacion;
import com.hotel.crm_hotel.models.Pago;
import com.hotel.crm_hotel.models.Reservacion;
import com.hotel.crm_hotel.models.TipoHabitacion;
import com.hotel.crm_hotel.models.Usuario;
import com.hotel.crm_hotel.repositories.ClienteRepository;
import com.hotel.crm_hotel.repositories.DetalleReservacionRepository;
import com.hotel.crm_hotel.repositories.HabitacionRepository;
import com.hotel.crm_hotel.repositories.PagoRepository;
import com.hotel.crm_hotel.repositories.ReservacionRepository;
import com.hotel.crm_hotel.repositories.TipoHabitacionRepository;
import com.hotel.crm_hotel.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ReservacionService {

    public static final String CONFIRMADA = "CONFIRMADA";
    public static final String CANCELADA = "CANCELADA";
    public static final String COMPLETADA = "COMPLETADA";
    private static final List<String> ESTADOS = List.of(CONFIRMADA, CANCELADA, COMPLETADA);
    private static final List<String> METODOS = List.of("TARJETA", "EFECTIVO");

    @Autowired
    private ReservacionRepository reservacionRepository;
    @Autowired
    private HabitacionRepository habitacionRepository;
    @Autowired
    private TipoHabitacionRepository tipoHabitacionRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private DetalleReservacionRepository detalleReservacionRepository;

    // RESERVA DESDE LA PAGINA PUBLICA: el cliente elige tipo, numero de habitacion y fechas.
    // Nunca pide IDs: el sistema los asigna y los maneja por dentro.
    @Transactional
    public ConfirmacionReserva crearDesdeWeb(ReservaRequest req) {
        if (vacio(req.nombre())) throw new IllegalArgumentException("El nombre es obligatorio");
        if (vacio(req.email()) || !req.email().contains("@")) throw new IllegalArgumentException("El correo no es válido");
        if (vacio(req.telefono())) throw new IllegalArgumentException("El teléfono es obligatorio");
        if (req.tipoHabitacionId() == null) throw new IllegalArgumentException("Selecciona un tipo de habitación");
        if (vacio(req.numeroHabitacion())) throw new IllegalArgumentException("Selecciona una habitación disponible");
        if (req.fechaEntrada() == null || req.fechaSalida() == null) throw new IllegalArgumentException("Las fechas son obligatorias");
        if (req.numeroHuespedes() == null) throw new IllegalArgumentException("Indica el número de personas");
        if (vacio(req.metodoPago())) throw new IllegalArgumentException("Selecciona un método de pago");

        if (req.fechaEntrada().isBefore(LocalDate.now())) throw new IllegalArgumentException("La fecha de entrada no puede ser anterior a hoy");
        if (!req.fechaSalida().isAfter(req.fechaEntrada())) throw new IllegalArgumentException("La fecha de salida debe ser posterior a la de entrada");

        String metodo = req.metodoPago().trim().toUpperCase();
        if (!METODOS.contains(metodo)) throw new IllegalArgumentException("Método de pago no válido");

        TipoHabitacion tipo = tipoHabitacionRepository.findById(req.tipoHabitacionId())
                .orElseThrow(() -> new IllegalArgumentException("El tipo de habitación no existe"));
        if (req.numeroHuespedes() < 1 || req.numeroHuespedes() > tipo.getCapacidadMaxima()) {
            throw new IllegalArgumentException("Este tipo de habitación admite máximo " + tipo.getCapacidadMaxima() + " personas");
        }

        Habitacion habitacion = habitacionRepository.findByNumero(req.numeroHabitacion().trim())
                .orElseThrow(() -> new IllegalArgumentException("La habitación seleccionada no existe"));
        if (!habitacion.getTipoHabitacion().getId().equals(tipo.getId())) {
            throw new IllegalArgumentException("La habitación seleccionada no corresponde al tipo elegido");
        }

        if (!reservacionRepository.findSolapadas(habitacion.getId(), CONFIRMADA, req.fechaEntrada(), req.fechaSalida()).isEmpty()) {
            throw new IllegalArgumentException("La habitación " + habitacion.getNumero() + " ya no está disponible en esas fechas");
        }

        // Cliente: si el correo ya existe se reutiliza; si no, se crea. El ID lo asigna la base de datos.
        Cliente cliente = clienteRepository.findFirstByEmailIgnoreCase(req.email().trim()).orElseGet(Cliente::new);
        cliente.setNombre(req.nombre().trim());
        cliente.setTelefono(req.telefono().trim());
        cliente.setEmail(req.email().trim());
        if (cliente.getFechaRegistro() == null) cliente.setFechaRegistro(LocalDateTime.now());
        cliente = clienteRepository.save(cliente);

        Usuario sistema = usuarioRepository.findByUsername("sistema_web")
                .orElseThrow(() -> new IllegalStateException("Falta el usuario sistema_web: reinicia la aplicación"));

        long noches = ChronoUnit.DAYS.between(req.fechaEntrada(), req.fechaSalida());
        double total = tipo.getPrecioNoche() * noches;

        LocalDateTime ahora = LocalDateTime.now();
        Reservacion reservacion = new Reservacion();
        reservacion.setCliente(cliente);
        reservacion.setHabitacion(habitacion);
        reservacion.setUsuario(sistema);
        reservacion.setFechaEntrada(req.fechaEntrada());
        reservacion.setFechaSalida(req.fechaSalida());
        reservacion.setNumeroHuespedes(req.numeroHuespedes());
        reservacion.setCosto(total);
        reservacion.setMetodoPago(metodo);
        reservacion.setStatus(CONFIRMADA);
        reservacion.setFechaCreacion(ahora);
        reservacion.setFechaActualizacion(ahora);
        reservacion = reservacionRepository.save(reservacion);

        // Pago pendiente: se liquida en recepcion o con el cobro en linea
        Pago pago = new Pago();
        pago.setReservacion(reservacion);
        pago.setMonto(total);
        pago.setMetodoPago(metodo);
        pago.setFecha(ahora);
        pago.setEstado("TARJETA".equals(metodo) ? "PAGADO" : "PENDIENTE");
        pagoRepository.save(pago);

        return new ConfirmacionReserva(habitacion.getNumero(), tipo.getNombre(),
                req.fechaEntrada(), req.fechaSalida(), (int) noches, total, metodo, CONFIRMADA);
    }

    // ===== ADMINISTRACION (ADMIN o RECEPCIONISTA) =====

    public List<Reservacion> obtenerTodos() {
        return reservacionRepository.findAll();
    }

    public Optional<Reservacion> obtenerById(Long id) {
        return reservacionRepository.findById(id);
    }

    @Transactional
    public Reservacion actualizar(Long id, Reservacion datos) {
        Reservacion r = reservacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservación no encontrada con ID: " + id));
        if (!vacio(datos.getStatus())) {
            String estado = datos.getStatus().trim().toUpperCase();
            if (!ESTADOS.contains(estado)) throw new IllegalArgumentException("Estado no válido: " + estado);
            r.setStatus(estado);
        }
        if (!vacio(datos.getMetodoPago())) {
            String metodo = datos.getMetodoPago().trim().toUpperCase();
            if (!METODOS.contains(metodo)) throw new IllegalArgumentException("Método de pago no válido");
            r.setMetodoPago(metodo);
        }
        r.setFechaActualizacion(LocalDateTime.now());
        return reservacionRepository.save(r);
    }

    @Transactional
    public Reservacion cancelarReservacion(Long id) {
        Reservacion r = reservacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservación no encontrada con ID: " + id));
        r.setStatus(CANCELADA);
        r.setFechaActualizacion(LocalDateTime.now());
        return reservacionRepository.save(r);
    }

    @Transactional
    public void eliminar(Long id) {
        Reservacion r = reservacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservación no encontrada con ID: " + id));
        // Antes se borran los registros que dependen de la reservacion (llaves foraneas)
        pagoRepository.deleteAll(pagoRepository.findByReservacionId(id));
        detalleReservacionRepository.deleteAll(detalleReservacionRepository.findByReservacionId(id));
        reservacionRepository.delete(r);
    }

    private boolean vacio(String s) {
        return s == null || s.isBlank();
    }
}
