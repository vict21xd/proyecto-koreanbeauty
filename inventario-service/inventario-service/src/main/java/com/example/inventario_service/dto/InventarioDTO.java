package com.example.inventario_service.dto;

import java.util.Date;

import jakarta.validation.constraints.*;
import lombok.*;

import com.example.inventario_service.model.Inventario;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioDTO {

    private Long id;

    @NotNull(message = "idProducto es obligatorio")
    private Long idProducto;

    @NotNull(message = "stock es obligatorio")
    @Min(value = 0, message = "stock no puede ser negativo")
    private Integer stock;

    @NotBlank(message = "ubicacion es obligatoria")
    private String ubicacion;

    private Date fechaActualizacion;

    public Inventario toModel() {
        return Inventario.builder()
                .id(id)
                .idProducto(idProducto)
                .stock(stock)
                .ubicacion(ubicacion)
                .fechaActualizacion(
                        fechaActualizacion != null ? fechaActualizacion : new Date()
                )
                .build();
    }

    public static InventarioDTO fromModel(Inventario i) {
        return InventarioDTO.builder()
                .id(i.getId())
                .idProducto(i.getIdProducto())
                .stock(i.getStock())
                .ubicacion(i.getUbicacion())
                .fechaActualizacion(i.getFechaActualizacion())
                .build();
    }
}