package com.newsaggregator.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public class PreferencesRequest {

    @NotEmpty(message = "At least one preference must be selected")
    private Set<String> preferences;

    // Constructors
    public PreferencesRequest() {}

    public PreferencesRequest(Set<String> preferences) {
        this.preferences = preferences;
    }

    // Getters and Setters
    public Set<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(Set<String> preferences) {
        this.preferences = preferences;
    }
} 