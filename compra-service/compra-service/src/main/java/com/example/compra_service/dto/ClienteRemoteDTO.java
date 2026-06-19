package com.example.compra_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClienteRemoteDTO {
    private Long id;
    private String nombre;
    private String correo;
    private String telefono;
    private String tipo_piel;
}
