package com.example.resena_service.service;

import com.example.resena_service.dto.ResenaDto;
import com.example.resena_service.exception.ResourceNotFoundException;
import com.example.resena_service.model.Resena;
import com.example.resena_service.repository.ResenaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResenaService {

    private final ResenaRepository repository;
    private final WebClient webClient;

    private static final Logger logger =
            LoggerFactory.getLogger(ResenaService.class);


    private final String PRODUCTO_URL = "http://localhost:7075/productos";
    private final String CLIENTE_URL = "http://localhost:7071/clientes";


    public List<ResenaDto> listar() {

        logger.info("INICIO: listar reseñas");

        List<ResenaDto> lista = repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();

        logger.info("FIN: {} reseñas encontradas", lista.size());

        return lista;
    }

    public ResenaDto buscarPorId(Long id) {

        logger.info("Buscando reseña ID: {}", id);

        Resena resena = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Reseña no encontrada ID: {}", id);
                    return new ResourceNotFoundException("Reseña no encontrada: " + id);
                });

        return toDTO(resena);
    }

    public ResenaDto guardar(ResenaDto dto) {

        logger.info("INICIO: crear reseña cliente {} producto {}",
                dto.getClienteId(), dto.getProductoId());

        validar(dto.getPuntuacion());

        validarProducto(dto.getProductoId());
        validarCliente(dto.getClienteId());

        Resena resena = Resena.builder()
                .clienteId(dto.getClienteId())
                .productoId(dto.getProductoId())
                .puntuacion(dto.getPuntuacion())
                .comentario(dto.getComentario())
                .fecha(LocalDate.now())
                .build();

        Resena guardada = repository.save(resena);

        logger.info("FIN: reseña creada ID: {}", guardada.getId());

        return toDTO(guardada);
    }

    public ResenaDto actualizar(Long id, ResenaDto dto) {

        logger.info("Actualizando reseña ID: {}", id);

        Resena resena = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada: " + id));

        validar(dto.getPuntuacion());

        resena.setPuntuacion(dto.getPuntuacion());
        resena.setComentario(dto.getComentario());

        Resena actualizada = repository.save(resena);

        return toDTO(actualizada);
    }


    public void eliminar(Long id) {

        logger.info("Eliminando reseña ID: {}", id);

        Resena resena = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada: " + id));

        repository.delete(resena);
    }

    private void validar(Integer puntuacion) {
        if (puntuacion == null || puntuacion < 1 || puntuacion > 5) {
            throw new IllegalArgumentException("Puntuación debe ser entre 1 y 5");
        }
    }

    private void validarProducto(Long productoId) {

        try {
            webClient.get()
                    .uri(PRODUCTO_URL + "/" + productoId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Producto no existe: " + productoId);
        }
    }

    private void validarCliente(Long clienteId) {

        try {
            webClient.get()
                    .uri(CLIENTE_URL + "/" + clienteId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Cliente no existe: " + clienteId);
        }
    }


    private ResenaDto toDTO(Resena r) {
        return ResenaDto.builder()
                .id(r.getId())
                .clienteId(r.getClienteId())
                .productoId(r.getProductoId())
                .puntuacion(r.getPuntuacion())
                .comentario(r.getComentario())
                .fecha(r.getFecha())
                .build();
    }
}