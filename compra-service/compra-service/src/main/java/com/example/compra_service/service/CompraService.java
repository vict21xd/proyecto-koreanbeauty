package com.example.compra_service.service;

import com.example.compra_service.exception.*;
import com.example.compra_service.model.*;
import com.example.compra_service.repository.CompraRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class CompraService {

    private static final Logger logger =
            LoggerFactory.getLogger(CompraService.class);

    private final CompraRepository repo;
    private final WebClient webClient;

    public CompraService(CompraRepository repo, WebClient webClient) {
        this.repo = repo;
        this.webClient = webClient;
    }

    public Compra guardar(Compra c) {

        logger.info("Creando compra cliente={}", c.getIdCliente());

        validar(c);
        calcular(c);

        return repo.save(c);
    }

    public List<Compra> listar() {
        return repo.findAll();
    }

    public Compra obtenerPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada"));
    }

    public Compra actualizar(Long id, Compra c) {

        Compra existente = obtenerPorId(id);

        c.setId(existente.getId());

        validar(c);
        calcular(c);

        return repo.save(c);
    }

    public void eliminar(Long id) {
        repo.delete(obtenerPorId(id));
    }


    private void validar(Compra c) {

        if (c.getIdCliente() == null)
            throw new BadRequestException("Cliente obligatorio");

        if (c.getDetalles() == null || c.getDetalles().isEmpty())
            throw new BadRequestException("Debe incluir productos");

        validarCliente(c.getIdCliente());

        for (DetalleCompra d : c.getDetalles()) {

            if (d.getCantidad() == null || d.getCantidad() <= 0)
                throw new BadRequestException("Cantidad inválida");

            if (d.getPrecioUnitario() == null || d.getPrecioUnitario() <= 0)
                throw new BadRequestException("Precio inválido");

            validarProducto(d.getIdProducto());

            d.setCompra(c);
        }
    }


    private void validarCliente(Long id) {
        webClient.get()
                .uri("http://localhost:7070/clientes/" + id + "/exists")
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

    private void validarProducto(Long id) {
        webClient.get()
                .uri("http://localhost:7073/productos/" + id + "/exists")
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }


    private void calcular(Compra c) {

        double total = 0;

        for (DetalleCompra d : c.getDetalles()) {
            d.setSubtotal(d.getCantidad() * d.getPrecioUnitario());
            total += d.getSubtotal();
        }

        c.setTotal(total);

        if (c.getFechaCompra() == null)
            c.setFechaCompra(new Date());
    }

    public Double totalEnRango(Date a, Date b) {
        return repo.findByFechaCompraBetween(a, b)
                .stream()
                .mapToDouble(x -> x.getTotal() == null ? 0 : x.getTotal())
                .sum();
    }
}