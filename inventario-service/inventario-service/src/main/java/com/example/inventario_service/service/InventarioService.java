package com.example.inventario_service.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.inventario_service.model.Inventario;
import com.example.inventario_service.repository.InventarioRepository;

@Service
public class InventarioService {

    private static final Logger logger =
            LoggerFactory.getLogger(InventarioService.class);

    private final InventarioRepository repository;

    public InventarioService(InventarioRepository repository) {
        this.repository = repository;
    }

    public Inventario guardar(Inventario inventario) {

        logger.info("INICIO: guardando inventario para producto {}", inventario.getIdProducto());

        if (inventario.getIdProducto() == null) {
            logger.error("ERROR: idProducto es null");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "idProducto obligatorio"
            );
        }

        inventario.setFechaActualizacion(new Date());

        Inventario guardado = repository.save(inventario);

        logger.info("OK: inventario guardado con id {}", guardado.getId());

        return guardado;
    }

    public List<Inventario> listar() {
        logger.info("CONSULTA: listando inventarios");
        return repository.findAll();
    }

    public Inventario obtenerPorId(Long id) {

        logger.info("CONSULTA: buscando inventario id {}", id);

        return repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("NO ENCONTRADO: inventario id {}", id);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Inventario no encontrado"
                    );
                });
    }

    public Inventario actualizar(Long id, Inventario inventario) {

        logger.info("ACTUALIZACIÓN: inventario id {}", id);

        Inventario existente = obtenerPorId(id);

        existente.setStock(inventario.getStock());
        existente.setUbicacion(inventario.getUbicacion());
        existente.setIdProducto(inventario.getIdProducto());
        existente.setFechaActualizacion(new Date());

        Inventario actualizado = repository.save(existente);

        logger.info("OK: inventario actualizado id {}", actualizado.getId());

        return actualizado;
    }

    public void eliminar(Long id) {

        logger.warn("ELIMINACIÓN: inventario id {}", id);

        Inventario existente = obtenerPorId(id);
        repository.delete(existente);

        logger.info("OK: inventario eliminado id {}", id);
    }

    public List<Inventario> listarPorProducto(Long idProducto) {

        logger.info("CONSULTA: inventario por producto {}", idProducto);

        return repository.findByIdProducto(idProducto);
    }
}