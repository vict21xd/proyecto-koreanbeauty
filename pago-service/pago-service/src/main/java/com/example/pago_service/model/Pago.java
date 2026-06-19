package com.example.pago_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idCompra;

    private Double monto;

    private String metodoPago;

    private String estado;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPago;
}