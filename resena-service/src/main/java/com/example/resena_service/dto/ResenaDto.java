package com.example.resena_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResenaDto {

    private Long id;

    @NotNull(message = "El cliente es obligatorio")
    @Positive(message = "El clienteId debe ser mayor a 0")
    private Long clienteId;

    @NotNull(message = "El producto es obligatorio")
    @Positive(message = "El productoId debe ser mayor a 0")
    private Long productoId;

    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 5, message = "La puntuación máxima es 5")
    private Integer puntuacion;

    @NotBlank(message = "El comentario es obligatorio")
    @Size(max = 500, message = "El comentario no puede superar 500 caracteres")
    private String comentario;

    private LocalDate fecha;
}