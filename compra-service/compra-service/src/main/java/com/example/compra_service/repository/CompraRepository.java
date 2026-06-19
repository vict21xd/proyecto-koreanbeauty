package com.example.compra_service.repository;

import com.example.compra_service.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {

    List<Compra> findByFechaCompraBetween(Date start, Date end);

    List<Compra> findByIdClienteIn(List<Long> ids);
}