package com.example.compra_service.dto;

import com.example.compra_service.model.Compra;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraDTO {

    private Long id;

    @NotNull(message = "idCliente obligatorio")
    private Long idCliente;

    private Double total;

    private Date fechaCompra;

    @Valid
    @NotEmpty(message = "Debe tener al menos un detalle")
    private List<DetalleCompraDTO> detalles;

    public Compra toModel() {
        Compra c = new Compra(
                id,
                idCliente,
                total,
                fechaCompra,
                detalles == null ? null :
                        detalles.stream().map(DetalleCompraDTO::toModel).collect(Collectors.toList())
        );

        if (c.getDetalles() != null) {
            c.getDetalles().forEach(d -> d.setCompra(c));
        }

        return c;
    }

    public static CompraDTO fromModel(Compra c) {
        if (c == null) return null;

        return CompraDTO.builder()
                .id(c.getId())
                .idCliente(c.getIdCliente())
                .total(c.getTotal())
                .fechaCompra(c.getFechaCompra())
                .detalles(
                        c.getDetalles() == null ? null :
                                c.getDetalles().stream()
                                        .map(DetalleCompraDTO::fromModel)
                                        .collect(Collectors.toList())
                )
                .build();
    }
}