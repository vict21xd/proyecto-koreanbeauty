package com.example.pago_service.repository;

import com.example.pago_service.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByEstadoIgnoreCase(String estado);
}