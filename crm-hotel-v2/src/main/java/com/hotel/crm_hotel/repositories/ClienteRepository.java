package com.hotel.crm_hotel.repositories;

import com.hotel.crm_hotel.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findFirstByEmailIgnoreCase(String email);
}
