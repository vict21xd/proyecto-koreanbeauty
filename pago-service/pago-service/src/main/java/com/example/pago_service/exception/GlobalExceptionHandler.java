package com.example.pago_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(ResponseStatusException ex) {

        Map<String, Object> body = new HashMap<>();

        body.put("timestamp", OffsetDateTime.now());
        body.put("status", ex.getStatusCode().value());
        body.put("error", ex.getReason());

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(body);
    }

  
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", OffsetDateTime.now());
        body.put("status", 400);
        body.put("error", "Error de validación");
        body.put("details", errors);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {

        Map<String, Object> body = new HashMap<>();

        body.put("timestamp", OffsetDateTime.now());
        body.put("status", 500);
        body.put("error", "Error interno del servidor");
        body.put("message", ex.getMessage());

        return ResponseEntity.status(500).body(body);
    }
}