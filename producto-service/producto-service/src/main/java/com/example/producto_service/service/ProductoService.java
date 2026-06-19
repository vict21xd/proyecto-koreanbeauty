package com.example.producto_service.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.example.producto_service.model.Producto;
import com.example.producto_service.repository.ProductoRepository;

@Slf4j
@Service
public class ProductoService {

    private final ProductoRepository repository;
    private final WebClient webClient;

    @Value("${api.inventario.stock}")
    private String inventarioStockPath;

    public ProductoService(ProductoRepository repository,
                           WebClient webClient) {
        this.repository = repository;
        this.webClient = webClient;
    }

    public Producto guardar(Producto p) {
        log.info("Creando producto {}", p.getNombre());
        validar(p);
        return repository.save(p);
    }

    public List<Producto> listar() {
        return repository.findAll();
    }

    public Producto obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Producto no encontrado: " + id
                        ));
    }

    public Producto actualizar(Long id, Producto p) {
        Producto existente = obtenerPorId(id);
        p.setId(existente.getId());
        validar(p);
        return repository.save(p);
    }

    public void eliminar(Long id) {
        repository.delete(obtenerPorId(id));
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre);
    }

    public boolean existePorId(Long id) {
        return repository.existsById(id);
    }

    public Integer obtenerStockDesdeInventario(Long idProducto) {

        try {
            return webClient.get()
                    .uri(String.format(inventarioStockPath, idProducto))
                    .retrieve()

                    .onStatus(status -> status.is4xxClientError(),
                            resp -> {
                                log.error("Error 4xx inventario {}", idProducto);
                                return resp.bodyToMono(String.class)
                                        .map(msg -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Producto no existe en inventario"
                                        ));
                            })

                    .onStatus(status -> status.is5xxServerError(),
                            resp -> {
                                log.error("Error 5xx inventario {}", idProducto);
                                return resp.bodyToMono(String.class)
                                        .map(msg -> new ResponseStatusException(
                                                HttpStatus.BAD_GATEWAY,
                                                "Inventario-service falló"
                                        ));
                            })

                    .bodyToMono(Integer.class)
                    .block();

        } catch (Exception e) {
            log.error("Error conexión inventario-service {}", idProducto);

            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Inventario-service no disponible"
            );
        }
    }

    public Double calcularPrecioConIVA(Double precio) {

        if (precio == null || precio <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Precio inválido"
            );
        }

        return Math.round(precio * 1.19 * 100.0) / 100.0;
    }

    private void validar(Producto p) {

        if (p.getNombre() == null ||
                !p.getNombre().matches("^[A-Za-zÁÉÍÓÚáéíóú ]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre inválido");
        }

        if (p.getPrecio() == null || p.getPrecio() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Precio inválido");
        }

        if (p.getStock() == null || p.getStock() < 0 || p.getStock() > 10000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock inválido");
        }

        if (p.getCategoria() == null || p.getCategoria().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría obligatoria");
        }

        if (!p.getCategoria().matches("^[A-Za-zÁÉÍÓÚáéíóú ]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría solo letras");
        }
    }
}