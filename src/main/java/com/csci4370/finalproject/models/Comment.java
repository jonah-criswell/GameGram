package com.csci4370.finalproject.models;

public class Comment extends BasicPost {

    /**
     * Constructs a Comment with specified details, leveraging the BasicPost structure.
     *
     * @param postId     the unique identifier of the comment
     * @param content    the text content of the comment
     * @param postDate   the creation date of the comment
     * @param user       the user who made the comment
     */
    public Comment(String postId, String content, String postDate, User user) {
        super(postId, content, postDate, user);
    }
}