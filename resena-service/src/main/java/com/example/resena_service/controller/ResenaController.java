package com.example.resena_service.controller;

import com.example.resena_service.dto.ResenaDto;
import com.example.resena_service.service.ResenaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/resenas")
@RequiredArgsConstructor
@Tag(name = "Reseñas", description = "Gestión de reseñas")
public class ResenaController {

    private final ResenaService service;

    private static final Logger logger =
            LoggerFactory.getLogger(ResenaController.class);

    @Operation(summary = "Listar reseñas")
    @GetMapping
    public CollectionModel<EntityModel<ResenaDto>> listar() {

        logger.info("GET /resenas");

        List<EntityModel<ResenaDto>> resenas = service.listar()
                .stream()
                .map(dto -> EntityModel.of(dto,

                        linkTo(methodOn(ResenaController.class)
                                .buscarPorId(dto.getId()))
                                .withSelfRel(),

                        linkTo(methodOn(ResenaController.class)
                                .actualizar(dto.getId(), dto))
                                .withRel("update"),

                        linkTo(methodOn(ResenaController.class)
                                .eliminar(dto.getId()))
                                .withRel("delete")
                ))
                .toList();

        return CollectionModel.of(
                resenas,
                linkTo(methodOn(ResenaController.class)
                        .listar()).withSelfRel()
        );
    }

    @Operation(summary = "Buscar reseña por ID")
    @GetMapping("/{id}")
    public EntityModel<ResenaDto> buscarPorId(@PathVariable Long id) {

        logger.info("GET /resenas/{}", id);

        ResenaDto dto = service.buscarPorId(id);

        return EntityModel.of(dto,

                linkTo(methodOn(ResenaController.class)
                        .buscarPorId(id))
                        .withSelfRel(),

                linkTo(methodOn(ResenaController.class)
                        .listar())
                        .withRel("all"),

                linkTo(methodOn(ResenaController.class)
                        .actualizar(id, dto))
                        .withRel("update"),

                linkTo(methodOn(ResenaController.class)
                        .eliminar(id))
                        .withRel("delete")
        );
    }

    @Operation(summary = "Crear reseña")
    @PostMapping
    public ResponseEntity<ResenaDto> guardar(
            @Valid @RequestBody ResenaDto dto) {

        logger.info("POST /resenas");

        return ResponseEntity.ok(service.guardar(dto));
    }

    @Operation(summary = "Actualizar reseña")
    @PutMapping("/{id}")
    public ResponseEntity<ResenaDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ResenaDto dto) {

        logger.info("PUT /resenas/{}", id);

        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar reseña")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        logger.info("DELETE /resenas/{}", id);

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}