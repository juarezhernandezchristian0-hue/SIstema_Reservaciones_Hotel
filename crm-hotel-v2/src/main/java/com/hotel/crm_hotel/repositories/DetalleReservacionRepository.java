package com.hotel.crm_hotel.repositories;

import com.hotel.crm_hotel.models.DetalleReservacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetalleReservacionRepository extends JpaRepository<DetalleReservacion, Long> {
    List<DetalleReservacion> findByReservacionId(Long reservacionId);
}
