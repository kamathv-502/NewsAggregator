package com.newsaggregator.controller;

import com.newsaggregator.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "*")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getNews() {
        try {
            String username = getCurrentUsername();
            List<Map<String, Object>> articles = newsService.getNewsForUser(username);
            
            return ResponseEntity.ok(Map.of(
                "username", username,
                "articles", articles,
                "count", articles.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchNews(@RequestParam String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Search query is required"));
            }
            
            List<Map<String, Object>> articles = newsService.searchNews(query.trim());
            
            return ResponseEntity.ok(Map.of(
                "query", query,
                "articles", articles,
                "count", articles.size()
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