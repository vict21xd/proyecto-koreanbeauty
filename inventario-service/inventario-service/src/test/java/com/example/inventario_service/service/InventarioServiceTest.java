package com.example.inventario_service.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.inventario_service.model.Inventario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InventarioServiceTest {

    @Autowired
    private InventarioService service;

    @Test
    void guardarInventario() {

        Inventario inventario =
                Inventario.builder()
                        .idProducto(100L)
                        .stock(20)
                        .ubicacion("Bodega Central")
                        .build();

        Inventario guardado =
                service.guardar(inventario);

        assertNotNull(guardado.getId());
    }
}