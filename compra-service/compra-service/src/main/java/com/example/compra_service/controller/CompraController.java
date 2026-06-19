package com.example.compra_service.controller;

import com.example.compra_service.dto.*;
import com.example.compra_service.model.Compra;
import com.example.compra_service.service.CompraService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/compras")
public class CompraController {

    private static final Logger logger =
            LoggerFactory.getLogger(CompraController.class);

    private final CompraService service;

    public CompraController(CompraService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CompraDTO> crear(@RequestBody CompraDTO dto) {

        logger.info("POST /compras");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CompraDTO.fromModel(service.guardar(dto.toModel())));
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<CompraDTO>>> listar() {

        List<EntityModel<CompraDTO>> lista = service.listar()
                .stream()
                .map(c -> EntityModel.of(
                        CompraDTO.fromModel(c),
                        linkTo(methodOn(CompraController.class).obtener(c.getId())).withSelfRel()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CompraDTO>> obtener(@PathVariable Long id) {

        Compra c = service.obtenerPorId(id);

        EntityModel<CompraDTO> model =
                EntityModel.of(CompraDTO.fromModel(c));

        model.add(linkTo(methodOn(CompraController.class).listar()).withRel("all"));

        return ResponseEntity.ok(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompraDTO> actualizar(@PathVariable Long id,
                                                @RequestBody CompraDTO dto) {

        return ResponseEntity.ok(
                CompraDTO.fromModel(service.actualizar(id, dto.toModel()))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}