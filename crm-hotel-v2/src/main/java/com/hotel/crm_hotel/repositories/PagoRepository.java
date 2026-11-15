package com.hotel.crm_hotel.repositories;

import com.hotel.crm_hotel.models.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByReservacionId(Long reservacionId);
}
