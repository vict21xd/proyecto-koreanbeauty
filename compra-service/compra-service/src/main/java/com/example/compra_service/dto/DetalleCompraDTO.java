package com.example.compra_service.dto;

import com.example.compra_service.model.DetalleCompra;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleCompraDTO {

    private Long id;

    @NotNull(message = "Producto obligatorio")
    private Long idProducto;

    @NotNull(message = "Cantidad obligatoria")
    @Min(value = 1)
    private Integer cantidad;

    @NotNull(message = "Precio obligatorio")
    @Positive
    private Double precioUnitario;

    private Double subtotal;

    public DetalleCompra toModel() {
        return new DetalleCompra(
                id,
                null,
                idProducto,
                cantidad,
                precioUnitario,
                subtotal
        );
    }

    public static DetalleCompraDTO fromModel(DetalleCompra d) {
        return DetalleCompraDTO.builder()
                .id(d.getId())
                .idProducto(d.getIdProducto())
                .cantidad(d.getCantidad())
                .precioUnitario(d.getPrecioUnitario())
                .subtotal(d.getSubtotal())
                .build();
    }
}