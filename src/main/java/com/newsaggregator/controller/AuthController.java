package com.newsaggregator.controller;

import com.newsaggregator.dto.AuthResponse;
import com.newsaggregator.dto.LoginRequest;
import com.newsaggregator.dto.RegisterRequest;
import com.newsaggregator.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            String message = userService.registerUser(request);
            return ResponseEntity.ok(new AuthResponse(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            String token = userService.loginUser(request);
            return ResponseEntity.ok(new AuthResponse(token, request.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse(e.getMessage()));
        }
    }
} 