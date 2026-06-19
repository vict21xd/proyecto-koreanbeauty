package com.example.compra_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalle_compras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compra_id")
    private Compra compra;

    private Long idProducto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}