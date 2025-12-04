package com.csci4370.finalproject.dto;

import com.csci4370.finalproject.models.Platform;
import java.util.List;
import java.util.stream.Collectors;

public class GameWithPlatforms {

    private String gameId;
    private String name;
    private String genre;
    private List<Platform> platforms;

    public GameWithPlatforms(String gameId, String name, String genre, List<Platform> platforms) {
        this.gameId = gameId;
        this.name = name;
        this.genre = genre;
        this.platforms = platforms;
    }

    // --- Getters and Setters ---
    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

 public String getPublisherList() {
    return platforms.stream()
                    .map(Platform::getPublisher)  
                    .distinct()
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
}

public String getPlatformListString() {
    return platforms.stream()
                    .map(Platform::getPlatformName)
                    .distinct()
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
}

    public int getYear() {
        return platforms.isEmpty() ? 0 : platforms.get(0).getYear();
    }
}
