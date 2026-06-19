package com.example.categoria_service.service;

import com.example.categoria_service.dto.CategoriaDto;
import com.example.categoria_service.exception.ResourceNotFoundException;
import com.example.categoria_service.model.Categoria;
import com.example.categoria_service.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private static final Logger logger =
            LoggerFactory.getLogger(CategoriaService.class);

    private final CategoriaRepository repository;

    public List<CategoriaDto> listar() {

        logger.info("INICIO listar categorías");

        List<CategoriaDto> lista = repository.findAll().stream()
                .map(this::toDTO)
                .toList();

        logger.info("FIN listar categorías. Total: {}", lista.size());

        return lista;
    }

    
    public CategoriaDto buscarPorId(Long id) {

        logger.info("Buscando categoría con id: {}", id);

        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Categoría no encontrada con id: {}", id);
                    return new ResourceNotFoundException(
                            "Categoría no encontrada con id: " + id);
                });

        logger.info("Categoría encontrada: {}", categoria.getNombre());

        return toDTO(categoria);
    }

    public CategoriaDto guardar(CategoriaDto dto) {

        logger.info("Creando categoría: {}", dto.getNombre());

        validarNegocio(dto);

        if (repository.existsByNombre(dto.getNombre().trim())) {
            logger.error("Categoría duplicada: {}", dto.getNombre());
            throw new IllegalArgumentException("La categoría ya existe");
        }

        dto.setNombre(dto.getNombre().trim().toUpperCase());

        Categoria categoria = Categoria.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();

        Categoria guardada = repository.save(categoria);

        logger.info("Categoría creada con ID: {}", guardada.getId());

        return toDTO(guardada);
    }

    public CategoriaDto actualizar(Long id, CategoriaDto dto) {

        logger.info("ctualizando categoría ID: {}", id);

        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("No se puede actualizar, no existe ID: {}", id);
                    return new ResourceNotFoundException(
                            "Categoría no encontrada con id: " + id);
                });

        validarNegocio(dto);

        if (!categoria.getNombre().equals(dto.getNombre().trim())
                && repository.existsByNombre(dto.getNombre().trim())) {

            logger.error("Nombre duplicado en actualización: {}", dto.getNombre());

            throw new IllegalArgumentException("La categoría ya existe");
        }

        categoria.setNombre(dto.getNombre().trim().toUpperCase());
        categoria.setDescripcion(dto.getDescripcion());

        Categoria actualizada = repository.save(categoria);

        logger.info("Categoría actualizada ID: {}", actualizada.getId());

        return toDTO(actualizada);
    }

    public void eliminar(Long id) {

        logger.info("Eliminando categoría ID: {}", id);

        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Categoría no encontrada para eliminar ID: {}", id);
                    return new ResourceNotFoundException(
                            "Categoría no encontrada con id: " + id);
                });

        if (categoria.getNombre().equalsIgnoreCase("SÉRUMS")) {

            logger.warn("Intento de eliminación bloqueado: {}", categoria.getNombre());

            throw new IllegalArgumentException(
                    "No se puede eliminar una categoría en uso");
        }

        repository.delete(categoria);

        logger.info("Categoría eliminada ID: {}", id);
    }

    private void validarNegocio(CategoriaDto dto) {

        logger.info("Validando negocio para: {}", dto.getNombre());

        if (dto.getNombre().trim().length() < 3) {
            logger.error("Nombre demasiado corto");
            throw new IllegalArgumentException("Nombre demasiado corto");
        }

        if (dto.getNombre().equalsIgnoreCase("string")) {
            logger.error("Nombre inválido detectado");
            throw new IllegalArgumentException("Nombre inválido");
        }
    }

    private CategoriaDto toDTO(Categoria categoria) {
        return CategoriaDto.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .build();
    }
}