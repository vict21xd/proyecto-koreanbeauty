package com.example.producto_service.dto;

import com.example.producto_service.model.Producto;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoDTO {

    private Long id;

    @NotBlank(message = "Nombre obligatorio")
    @Size(min = 3, max = 100)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóú ]+$", message = "Solo letras en nombre")
    private String nombre;

    @NotBlank(message = "Descripción obligatoria")
    @Size(min = 5, max = 255)
    private String descripcion;

    @NotNull
    @Positive(message = "Precio debe ser mayor a 0")
    private Double precio;

    @NotNull
    @Min(value = 0)
    @Max(value = 10000)
    private Integer stock;

    @NotBlank(message = "Categoría obligatoria")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóú ]+$", message = "Solo letras en categoría")
    private String categoria;

    public Producto toModel() {
        return Producto.builder()
                .id(id)
                .nombre(nombre)
                .descripcion(descripcion)
                .precio(precio)
                .stock(stock)
                .categoria(categoria)
                .build();
    }

    public static ProductoDTO fromModel(Producto p) {
        return ProductoDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .descripcion(p.getDescripcion())
                .precio(p.getPrecio())
                .stock(p.getStock())
                .categoria(p.getCategoria())
                .build();
    }
}