package com.hotel.crm_hotel.repositories;

import com.hotel.crm_hotel.models.Reservacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservacionRepository extends JpaRepository<Reservacion, Long> {

    // Reservaciones activas que se traslapan con el rango pedido (ocupan la habitación)
    @Query("SELECT r FROM Reservacion r WHERE r.habitacion.id = :habitacionId AND r.status = :estado " +
           "AND r.fechaEntrada < :salida AND r.fechaSalida > :entrada")
    List<Reservacion> findSolapadas(@Param("habitacionId") Long habitacionId,
                                    @Param("estado") String estado,
                                    @Param("entrada") LocalDate entrada,
                                    @Param("salida") LocalDate salida);

    boolean existsByHabitacionId(Long habitacionId);
}
