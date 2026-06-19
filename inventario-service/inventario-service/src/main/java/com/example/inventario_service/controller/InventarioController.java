package com.example.inventario_service.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.inventario_service.dto.InventarioDTO;
import com.example.inventario_service.model.Inventario;
import com.example.inventario_service.service.InventarioService;

@RestController
@RequestMapping("/inventarios")
public class InventarioController {

    private static final Logger logger =
            LoggerFactory.getLogger(InventarioController.class);

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @PostMapping
    public ResponseEntity<InventarioDTO> crear(
            @Valid @RequestBody InventarioDTO dto) {

        logger.info("POST /inventarios");

        Inventario saved = inventarioService.guardar(dto.toModel());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(InventarioDTO.fromModel(saved));
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<InventarioDTO>>> listar() {

        logger.info("GET /inventarios");

        List<EntityModel<InventarioDTO>> lista =
                inventarioService.listar()
                        .stream()
                        .map(i -> {

                            InventarioDTO dto =
                                    InventarioDTO.fromModel(i);

                            return EntityModel.of(
                                    dto,
                                    linkTo(
                                            methodOn(
                                                    InventarioController.class)
                                                    .obtener(i.getId()))
                                            .withSelfRel()
                            );
                        })
                        .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<InventarioDTO>> obtener(
            @PathVariable Long id) {

        logger.info("GET /inventarios/{}", id);

        InventarioDTO dto =
                InventarioDTO.fromModel(
                        inventarioService.obtenerPorId(id)
                );

        EntityModel<InventarioDTO> model =
                EntityModel.of(dto);

        model.add(
                linkTo(
                        methodOn(
                                InventarioController.class)
                                .obtener(id))
                        .withSelfRel()
        );

        model.add(
                linkTo(
                        methodOn(
                                InventarioController.class)
                                .listar())
                        .withRel("all")
        );

        model.add(
                linkTo(
                        methodOn(
                                InventarioController.class)
                                .actualizar(id, dto))
                        .withRel("update")
        );

        model.add(
                linkTo(
                        methodOn(
                                InventarioController.class)
                                .eliminar(id))
                        .withRel("delete")
        );

        return ResponseEntity.ok(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody InventarioDTO dto) {

        logger.info("PUT /inventarios/{}", id);

        return ResponseEntity.ok(
                InventarioDTO.fromModel(
                        inventarioService.actualizar(
                                id,
                                dto.toModel()
                        )
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        logger.info("DELETE /inventarios/{}", id);

        inventarioService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

        @GetMapping("/producto/{id}/stock")
        public ResponseEntity<Integer> stock(@PathVariable Long id) {

        int total = inventarioService.listarPorProducto(id)
                .stream()
                .mapToInt(i -> i.getStock() == null ? 0 : i.getStock())
                .sum();

        return ResponseEntity.ok(total);
        }
}