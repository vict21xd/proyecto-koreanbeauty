package com.example.categoria_service.controller;

import com.example.categoria_service.dto.CategoriaDto;
import com.example.categoria_service.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private static final Logger logger =
            LoggerFactory.getLogger(CategoriaController.class);

    private final CategoriaService service;

    @Operation(summary = "Listar categorías")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CategoriaDto>>> listar() {

        logger.info("GET /categorias");

        List<EntityModel<CategoriaDto>> categorias = service.listar()
                .stream()
                .map(dto -> EntityModel.of(dto,
                        linkTo(methodOn(CategoriaController.class)
                                .buscarPorId(dto.getId())).withSelfRel(),
                        linkTo(methodOn(CategoriaController.class)
                                .actualizar(dto.getId(), dto)).withRel("update"),
                        linkTo(methodOn(CategoriaController.class)
                                .eliminar(dto.getId())).withRel("delete")
                ))
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(categorias,
                        linkTo(methodOn(CategoriaController.class).listar()).withSelfRel()
                )
        );
    }

    @Operation(summary = "Buscar categoría por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CategoriaDto>> buscarPorId(@PathVariable Long id) {

        logger.info("GET /categorias/{}", id);

        CategoriaDto dto = service.buscarPorId(id);

        EntityModel<CategoriaDto> resource = EntityModel.of(dto,
                linkTo(methodOn(CategoriaController.class)
                        .buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(CategoriaController.class)
                        .listar()).withRel("all"),
                linkTo(methodOn(CategoriaController.class)
                        .actualizar(id, dto)).withRel("update"),
                linkTo(methodOn(CategoriaController.class)
                        .eliminar(id)).withRel("delete")
        );

        return ResponseEntity.ok(resource);
    }

    @PostMapping
    public ResponseEntity<CategoriaDto> guardar(@Valid @RequestBody CategoriaDto dto) {

        logger.info("POST /categorias");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaDto dto) {

        logger.info("PUT /categorias/{}", id);

        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        logger.info("DELETE /categorias/{}", id);

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}