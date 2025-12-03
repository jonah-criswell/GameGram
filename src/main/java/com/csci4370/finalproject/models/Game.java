package com.csci4370.finalproject.models;

/**
 * Represents a game of the micro blogging platform.
 */
public class Game {

    private final String game_id;

    private final String name;

    private final int year;

    private final String platform;

    private final String genre;

    private final String publisher;

    /**
     * Constructs a User with specified details.
     *
     * @param game_id   the unique identifier of the game
     * @param name      the name of the game
     * @param year      the release year of the game
     * @param platform  the platform of the game
     * @param genre     the genre of the game
     * @param publisher the publisher of the game 
     */
    public Game(String game_id, String name, int year, String platform, String genre, String publisher) {
        this.game_id = game_id;
        this.name = name;
        this.year = year;
        this.platform = platform;
        this.genre = genre;
        this.publisher = publisher;
    }
}

 
