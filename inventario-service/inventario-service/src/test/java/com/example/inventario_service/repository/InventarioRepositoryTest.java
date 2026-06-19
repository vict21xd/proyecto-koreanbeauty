package com.example.inventario_service.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.inventario_service.model.Inventario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class InventarioRepositoryTest {

    @Autowired
    private InventarioRepository repository;

    @Test
    void repositoryNotNull() {
        assertNotNull(repository);
    }

    @Test
    void saveAndFindInventario() {

        Inventario inv = new Inventario();
        inv.setIdProducto(1L);
        inv.setStock(10);
        inv.setUbicacion("Bodega Test");
        inv.setFechaActualizacion(new java.util.Date());

        Inventario saved = repository.save(inv);

        assertNotNull(saved.getId());
        assertEquals(1L, saved.getIdProducto());
    }

    @Test
    void findByIdProducto_shouldReturnList() {

        List<Inventario> list = repository.findByIdProducto(1L);

        assertNotNull(list);
    }
}