package com.example.compra_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.compra_service.model.DetalleCompra;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {

}
