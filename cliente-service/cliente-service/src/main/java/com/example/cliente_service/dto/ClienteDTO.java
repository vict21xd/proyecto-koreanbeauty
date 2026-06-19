package com.example.cliente_service.dto;

import com.example.cliente_service.model.Cliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {

    private Long id;

    @NotBlank(message = "Nombre obligatorio")
    @Size(min = 3, max = 50,
            message = "El nombre debe tener entre 3 y 50 caracteres")
    @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$",
            message = "El nombre solo puede contener letras"
    )
    private String nombre;

    @Email(message = "Correo inválido")
    @NotBlank(message = "Correo obligatorio")
    private String correo;

    @NotBlank(message = "Teléfono obligatorio")
    @Pattern(
            regexp = "^[0-9]{8,15}$",
            message = "Teléfono inválido"
    )
    private String telefono;

    @NotBlank(message = "Tipo de piel obligatorio")
    @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$",
            message = "Tipo de piel inválido"
    )
    private String tipoPiel;

    public Cliente toModel() {
        return Cliente.builder()
                .id(id)
                .nombre(nombre)
                .correo(correo)
                .telefono(telefono)
                .tipoPiel(tipoPiel)
                .build();
    }

    public static ClienteDTO fromModel(Cliente c) {
        return ClienteDTO.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .correo(c.getCorreo())
                .telefono(c.getTelefono())
                .tipoPiel(c.getTipoPiel())
                .build();
    }
}