package com.fatbike.shop.controller;

import com.fatbike.shop.dto.auth.LoginRequest;
import com.fatbike.shop.dto.auth.LoginResponse;
import com.fatbike.shop.dto.auth.RegisterRequest;
import com.fatbike.shop.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        // Delegate sign-up workflow to the service and return 201 when successful
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        // Issue a JWT + role bundle that the client can store for subsequent calls
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
