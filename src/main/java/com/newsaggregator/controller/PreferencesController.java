package com.newsaggregator.controller;

import com.newsaggregator.dto.PreferencesRequest;
import com.newsaggregator.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/preferences")
@CrossOrigin(origins = "*")
public class PreferencesController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getPreferences() {
        try {
            String username = getCurrentUsername();
            Set<String> preferences = userService.getUserPreferences(username);
            
            return ResponseEntity.ok(Map.of(
                "username", username,
                "preferences", preferences
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> updatePreferences(@Valid @RequestBody PreferencesRequest request) {
        try {
            String username = getCurrentUsername();
            userService.updateUserPreferences(username, request.getPreferences());
            
            return ResponseEntity.ok(Map.of(
                "message", "Preferences updated successfully",
                "username", username,
                "preferences", request.getPreferences()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        throw new RuntimeException("User not authenticated");
    }
} 