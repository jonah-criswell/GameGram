package com.csci4370.finalproject.models;
/**
 * Represents a review of a game.
 */
public class Review extends BasicPost {

    private final int game_id;

    private final int hoursPlayed;

    private final int reviewRating;

    private final String gameName;

    private final int heartsCount;

    private final int commentsCount;

    private final boolean isHearted;

    private final boolean isBookmarked;

    /**
     * Constructs a Review with specified details.
     * 
     * @param postId     the unique identifier of the post
     * @param content    the text content of the post
     * @param postDate   the creation date of the post
     * @param user       the user who created the post
     * @param game_id      the unique identifier of the game being reviewed
     * @param hoursPlayed the number of hours played by the reviewer
     * @param reviewText  the text content of the review
     * @param reviewRating the rating given in the review
     * @param heartsCount the number of hearts (likes) the review has received
     * @param commentsCount the number of comments the review has received
     * @param isHearted   whether the review is hearted by the current user
     * @param isBookmarked whether the review is bookmarked by the current user
     * @param gameName    the name of the game being reviewed
     * @param reviewDate  the date the review was posted
     */
    public Review(String postId, String content, String postDate, User user, int game_id, int hoursPlayed,
            int reviewRating, String gameName, int heartsCount, int commentsCount, boolean isHearted, boolean isBookmarked) {
        super(postId, content, postDate, user);
        this.game_id = game_id;
        this.hoursPlayed = hoursPlayed;
        this.reviewRating = reviewRating;
        this.gameName = gameName;
        this.heartsCount = heartsCount;
        this.commentsCount = commentsCount;
        this.isHearted = isHearted;
        this.isBookmarked = isBookmarked;
    }

    /**
     * Returns the review rating.
     * 
     * @return  the review rating
     */
    public int getReviewRating() {
        return reviewRating;
    }

    /**
     * Returns the game ID.
     *  
     * @return the game ID
     */
    public int getGame_id() {
        return game_id;
    }
    /**
     * Returns the number of hours played.
     *
     * @return the number of hours played
     */
    public int getHoursPlayed() {
        return hoursPlayed;
    }


    /**
     * Returns the number of hearts (likes) the post has received.
     *
     * @return the number of hearts
     */
    public int getHeartsCount() {
        return heartsCount;
    }

    /**
     * Returns the name of the game being reviewed.
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * Returns the number of comments the post has received.
     *
     * @return the number of comments
     */
    public int getCommentsCount() {
        return commentsCount;
    }

    /**
     * Returns whether the post is hearted by the current user.
     *
     * @return true if the post is hearted, false otherwise
     */
    public boolean getHearted() {
        return isHearted;
    }

    /**
     * Returns whether the post is bookmarked by the current user.
     *
     * @return true if the post is bookmarked, false otherwise
     */
    public boolean isBookmarked() {
        return isBookmarked;
    }

}

