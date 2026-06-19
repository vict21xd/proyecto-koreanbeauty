package com.example.inventario_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idProducto;

    private Integer stock;

    private String ubicacion;

    @Temporal(TemporalType.DATE)
    private Date fechaActualizacion;
}