package com.example.inventario_service.exception;

import java.time.OffsetDateTime;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(
            ResponseStatusException ex) {

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(
                        Map.of(
                                "timestamp", OffsetDateTime.now(),
                                "status", ex.getStatusCode().value(),
                                "error", ex.getReason()
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(
            Exception ex) {

        return ResponseEntity.internalServerError()
                .body(
                        Map.of(
                                "timestamp", OffsetDateTime.now(),
                                "status", 500,
                                "error", "Error interno del servidor",
                                "message", ex.getMessage()
                        )
                );
    }
}