package com.csci4370.finalproject.models;

/**
 * Represents a user of the micro blogging platform.
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
     * @param userId           the unique identifier of the user
     * @param firstName        the first name of the user
     * @param lastName         the last name of the user
     * @param profileImagePath the path of the profile image file for the user
     */
    public Game(String game_id, String name, int year, String platform, String genre, String publisher) {
        this.game_id = game_id;
        this.name = name;
        this.year = year;
        this.platform = platform;
        this.genre = genre;
        this.publisher = publisher;
    }

 
