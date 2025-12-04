package com.csci4370.finalproject.models;

/**
 * Represents a user of the micro blogging platform.
 */
public class Game {

    private final String game_id;

    private final String name;

    private final String genre;
    
    public Game(String game_id, String name, String genre) {
        this.game_id = game_id;
        this.name = name;
        this.genre = genre;
    }
}

 
