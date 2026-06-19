package com.example.pago_service.dto;

import com.example.pago_service.model.Pago;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoDTO {

    private Long id;

    @NotNull(message = "idCompra obligatorio")
    private Long idCompra;

    @NotNull
    @Positive(message = "monto debe ser mayor a 0")
    private Double monto;

    @NotBlank(message = "metodoPago obligatorio")
    private String metodoPago;

    @NotBlank(message = "estado obligatorio")
    @Pattern(regexp = "PAGADO|PENDIENTE|CANCELADO",
            message = "Estado inválido")
    private String estado;

    private Date fechaPago;

    public Pago toModel() {
        return Pago.builder()
                .id(id)
                .idCompra(idCompra)
                .monto(monto)
                .metodoPago(metodoPago.toUpperCase())
                .estado(estado.toUpperCase())
                .fechaPago(fechaPago)
                .build();
    }

    public static PagoDTO fromModel(Pago pago) {
        return PagoDTO.builder()
                .id(pago.getId())
                .idCompra(pago.getIdCompra())
                .monto(pago.getMonto())
                .metodoPago(pago.getMetodoPago())
                .estado(pago.getEstado())
                .fechaPago(pago.getFechaPago())
                .build();
    }
}