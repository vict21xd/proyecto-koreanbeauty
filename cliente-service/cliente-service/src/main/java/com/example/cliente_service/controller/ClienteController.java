package com.example.cliente_service.controller;

import com.example.cliente_service.dto.ClienteDTO;
import com.example.cliente_service.model.Cliente;
import com.example.cliente_service.service.ClienteService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private static final Logger logger =
            LoggerFactory.getLogger(ClienteController.class);

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> crear(
            @Valid @RequestBody ClienteDTO dto) {

        logger.info("POST /clientes");

        Cliente cliente = service.guardar(dto.toModel());

        return ResponseEntity.ok(
                ClienteDTO.fromModel(cliente)
        );
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ClienteDTO>>> listar() {

        logger.info("GET /clientes");

        List<EntityModel<ClienteDTO>> clientes = service.listar()
                .stream()
                .map(cliente -> {

                    ClienteDTO dto =
                            ClienteDTO.fromModel(cliente);

                    return EntityModel.of(
                            dto,
                            linkTo(methodOn(ClienteController.class)
                                    .buscarPorId(dto.getId()))
                                    .withSelfRel(),

                            linkTo(methodOn(ClienteController.class)
                                    .actualizar(dto.getId(), dto))
                                    .withRel("update"),

                            linkTo(methodOn(ClienteController.class)
                                    .eliminar(dto.getId()))
                                    .withRel("delete")
                    );
                })
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(
                        clientes,
                        linkTo(methodOn(ClienteController.class)
                                .listar())
                                .withSelfRel()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ClienteDTO>> buscarPorId(
            @PathVariable Long id) {

        logger.info("GET /clientes/{}", id);

        Cliente cliente = service.obtenerPorId(id);

        ClienteDTO dto =
                ClienteDTO.fromModel(cliente);

        EntityModel<ClienteDTO> resource =
                EntityModel.of(
                        dto,

                        linkTo(methodOn(ClienteController.class)
                                .buscarPorId(id))
                                .withSelfRel(),

                        linkTo(methodOn(ClienteController.class)
                                .listar())
                                .withRel("all"),

                        linkTo(methodOn(ClienteController.class)
                                .actualizar(id, dto))
                                .withRel("update"),

                        linkTo(methodOn(ClienteController.class)
                                .eliminar(id))
                                .withRel("delete")
                );

        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteDTO dto) {

        logger.info("PUT /clientes/{}", id);

        Cliente actualizado =
                service.actualizar(id, dto.toModel());

        return ResponseEntity.ok(
                ClienteDTO.fromModel(actualizado)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        logger.info("DELETE /clientes/{}", id);

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existe(
            @PathVariable Long id) {

        logger.info("GET /clientes/{}/exists", id);

        return ResponseEntity.ok(
                service.existePorId(id)
        );
    }
}