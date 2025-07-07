package com.newsaggregator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Autowired
    private UserService userService;

    @Value("${news.api.key}")
    private String apiKey;

    @Value("${news.api.base-url}")
    private String baseUrl;

    @Value("${news.api.timeout}")
    private int timeout;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public NewsService() {
        this.webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public List<Map<String, Object>> getNewsForUser(String username) {
        Set<String> preferences = userService.getUserPreferences(username);
        
        if (preferences.isEmpty()) {
            // Return general news if no preferences set
            return getNewsByCategory("general");
        }

        List<Map<String, Object>> allNews = new ArrayList<>();
        
        for (String preference : preferences) {
            try {
                List<Map<String, Object>> categoryNews = getNewsByCategory(preference);
                allNews.addAll(categoryNews);
            } catch (Exception e) {
                // Log error but continue with other categories
                System.err.println("Error fetching news for category: " + preference + " - " + e.getMessage());
            }
        }

        // Remove duplicates and limit results
        return allNews.stream()
                .distinct()
                .limit(50)
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> getNewsByCategory(String category) {
        try {
            String url = baseUrl + "/top-headlines?country=us&category=" + category + "&apiKey=" + apiKey;
            
            String response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.has("articles")) {
                List<Map<String, Object>> articles = new ArrayList<>();
                JsonNode articlesNode = jsonNode.get("articles");
                
                for (JsonNode article : articlesNode) {
                    Map<String, Object> articleMap = new HashMap<>();
                    articleMap.put("title", article.get("title").asText());
                    articleMap.put("description", article.get("description").asText());
                    articleMap.put("url", article.get("url").asText());
                    articleMap.put("urlToImage", article.get("urlToImage").asText());
                    articleMap.put("publishedAt", article.get("publishedAt").asText());
                    articleMap.put("source", article.get("source").get("name").asText());
                    articleMap.put("category", category);
                    
                    articles.add(articleMap);
                }
                
                return articles;
            }
            
        } catch (Exception e) {
            System.err.println("Error fetching news for category " + category + ": " + e.getMessage());
        }
        
        return new ArrayList<>();
    }

    public List<Map<String, Object>> searchNews(String query) {
        try {
            String url = baseUrl + "/everything?q=" + query + "&apiKey=" + apiKey;
            
            String response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.has("articles")) {
                List<Map<String, Object>> articles = new ArrayList<>();
                JsonNode articlesNode = jsonNode.get("articles");
                
                for (JsonNode article : articlesNode) {
                    Map<String, Object> articleMap = new HashMap<>();
                    articleMap.put("title", article.get("title").asText());
                    articleMap.put("description", article.get("description").asText());
                    articleMap.put("url", article.get("url").asText());
                    articleMap.put("urlToImage", article.get("urlToImage").asText());
                    articleMap.put("publishedAt", article.get("publishedAt").asText());
                    articleMap.put("source", article.get("source").get("name").asText());
                    
                    articles.add(articleMap);
                }
                
                return articles.stream().limit(20).collect(Collectors.toList());
            }
            
        } catch (Exception e) {
            System.err.println("Error searching news: " + e.getMessage());
        }
        
        return new ArrayList<>();
    }
} 