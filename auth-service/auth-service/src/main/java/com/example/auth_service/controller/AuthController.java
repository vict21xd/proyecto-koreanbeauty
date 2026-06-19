package com.example.auth_service.controller;

import com.example.auth_service.dto.LoginRequest;
import com.example.auth_service.dto.RegisterRequest;
import com.example.auth_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {

        String token = userService.login(request.getEmail(), request.getPassword());

        Map<String, String> resp = new HashMap<>();

        if (token == null) {
            resp.put("status", "error");
            resp.put("message", "Credenciales inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }

        resp.put("status", "ok");
        resp.put("token", token);

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {

        String result = userService.register(request.getEmail(), request.getPassword());

        Map<String, String> resp = new HashMap<>();
        resp.put("message", result);

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

}