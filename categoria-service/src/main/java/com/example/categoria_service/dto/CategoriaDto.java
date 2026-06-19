package com.example.categoria_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaDto {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 50,
            message = "El nombre debe tener entre 3 y 50 caracteres")
    @Pattern(
            regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ ]+$",
            message = "El nombre solo puede contener letras y espacios"
    )
    private String nombre;

    @Size(max = 200,
            message = "La descripción no puede superar los 200 caracteres")
    private String descripcion;
}