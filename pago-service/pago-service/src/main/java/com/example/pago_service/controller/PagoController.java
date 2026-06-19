package com.example.pago_service.controller;

import com.example.pago_service.dto.PagoDTO;
import com.example.pago_service.model.Pago;
import com.example.pago_service.service.PagoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Slf4j
@RestController
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService service;

    public PagoController(PagoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EntityModel<PagoDTO>> crear(@Valid @RequestBody PagoDTO dto) {

        Pago pago = service.guardar(dto.toModel());
        PagoDTO response = PagoDTO.fromModel(pago);

        EntityModel<PagoDTO> model = EntityModel.of(response);

        model.add(linkTo(methodOn(PagoController.class)
                .obtener(pago.getId())).withSelfRel());

        model.add(linkTo(methodOn(PagoController.class)
                .listar()).withRel("all"));

        return ResponseEntity.ok(model);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PagoDTO>>> listar() {

        List<EntityModel<PagoDTO>> pagos = service.listar()
                .stream()
                .map(p -> {

                    PagoDTO dto = PagoDTO.fromModel(p);

                    return EntityModel.of(dto,
                            linkTo(methodOn(PagoController.class)
                                    .obtener(p.getId())).withSelfRel()
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(pagos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PagoDTO>> obtener(@PathVariable Long id) {

        Pago pago = service.obtenerPorId(id);
        PagoDTO dto = PagoDTO.fromModel(pago);

        EntityModel<PagoDTO> model = EntityModel.of(dto);

        model.add(linkTo(methodOn(PagoController.class)
                .listar()).withRel("all"));

        model.add(linkTo(methodOn(PagoController.class)
                .actualizar(id, dto)).withRel("update"));

        model.add(linkTo(methodOn(PagoController.class)
                .eliminar(id)).withRel("delete"));

        return ResponseEntity.ok(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PagoDTO dto) {

        return ResponseEntity.ok(
                PagoDTO.fromModel(service.actualizar(id, dto.toModel()))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PagoDTO>> buscar(@PathVariable String estado) {

        return ResponseEntity.ok(
                service.buscarPorEstado(estado)
                        .stream()
                        .map(PagoDTO::fromModel)
                        .toList()
        );
    }
}