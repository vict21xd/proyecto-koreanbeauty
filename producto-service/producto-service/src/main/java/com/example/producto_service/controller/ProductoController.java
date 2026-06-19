package com.example.producto_service.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.producto_service.dto.ProductoDTO;
import com.example.producto_service.service.ProductoService;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private static final Logger logger =
            LoggerFactory.getLogger(ProductoController.class);

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EntityModel<ProductoDTO>> crear(
            @Valid @RequestBody ProductoDTO dto) {

        logger.info("POST /productos - creando producto: {}", dto.getNombre());

        var saved = service.guardar(dto.toModel());

        logger.info("Producto creado con ID={}", saved.getId());

        ProductoDTO response = ProductoDTO.fromModel(saved);

        EntityModel<ProductoDTO> model = EntityModel.of(response);

        model.add(linkTo(methodOn(ProductoController.class)
                .obtenerPorId(saved.getId())).withSelfRel());

        model.add(linkTo(methodOn(ProductoController.class)
                .listarProductos()).withRel("all"));

        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ProductoDTO>>> listarProductos() {

        logger.info("GET /productos - listando productos");

        var productos = service.listar().stream()
                .map(p -> {
                    ProductoDTO dto = ProductoDTO.fromModel(p);

                    EntityModel<ProductoDTO> model = EntityModel.of(dto);

                    model.add(linkTo(methodOn(ProductoController.class)
                            .obtenerPorId(p.getId())).withSelfRel());

                    model.add(linkTo(methodOn(ProductoController.class)
                            .listarProductos()).withRel("all"));

                    return model;
                })
                .toList();

        logger.info("Total productos encontrados: {}", productos.size());

        return ResponseEntity.ok(CollectionModel.of(productos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProductoDTO>> obtenerPorId(@PathVariable Long id) {

        logger.info("GET /productos/{} - buscando producto", id);

        var p = service.obtenerPorId(id);

        logger.info("Producto encontrado ID={}", id);

        ProductoDTO dto = ProductoDTO.fromModel(p);

        EntityModel<ProductoDTO> model = EntityModel.of(dto);

        model.add(linkTo(methodOn(ProductoController.class)
                .listarProductos()).withRel("all"));

        model.add(linkTo(methodOn(ProductoController.class)
                .eliminar(id)).withRel("delete"));

        return ResponseEntity.ok(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoDTO dto) {

        logger.info("PUT /productos/{} - actualizando producto", id);

        var updated = service.actualizar(id, dto.toModel());

        logger.info("Producto actualizado ID={}", id);

        return ResponseEntity.ok(ProductoDTO.fromModel(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        logger.info("DELETE /productos/{} - eliminando producto", id);

        service.eliminar(id);

        logger.info("Producto eliminado ID={}", id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductoDTO>> buscar(@RequestParam String nombre) {

        logger.info("GET /productos/search?nombre={}", nombre);

        var result = service.buscarPorNombre(nombre);

        logger.info("Resultados encontrados: {}", result.size());

        return ResponseEntity.ok(
                result.stream()
                        .map(ProductoDTO::fromModel)
                        .toList()
        );
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existe(@PathVariable Long id) {

        logger.info("GET /productos/{}/exists", id);

        return ResponseEntity.ok(service.existePorId(id));
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<Integer> stock(@PathVariable Long id) {

        logger.info("GET /productos/{}/stock", id);

        return ResponseEntity.ok(service.obtenerStockDesdeInventario(id));
    }
}