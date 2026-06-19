package com.example.cliente_service.exception;

public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException(Long id) {
        super("Cliente no encontrado con id: " + id);
    }
}