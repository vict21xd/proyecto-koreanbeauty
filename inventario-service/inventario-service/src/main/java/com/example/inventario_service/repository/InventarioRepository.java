package com.example.inventario_service.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.inventario_service.model.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    List<Inventario> findByIdProducto(Long idProducto);
}