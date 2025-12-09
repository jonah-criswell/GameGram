package com.csci4370.finalproject.models;





/**
 * Extends the User class to include a following status,
 * indicating whether the current session user follows this user.
 */
public class FollowableUser extends User {

    /**
     * Flag indicating whether the user is followed by the current session user.
     */
    private final boolean isFollowed;

    /**
     * This is the date and time that this user has last made a post.
     */
    private final String lastActiveDate;

    /**
     * Recent reviews from this user.
     */
    private Review recentGame;

    /**
     * Constructs a FollowableUser with specified details and follow status.
     *
     * @param userId           the unique identifier of the user
     * @param firstName        the first name of the user
     * @param lastName         the last name of the user
     * @param profileImageName the name of the profile image file for the user
     * @param isFollowed       the follow status of the user by the current session
     *                         user
     * @param lastActiveDate   the date and time that this user has last made a post.
     * @param recentGame      recent reviews from this user
     */
    public FollowableUser(String userId, String firstName, String lastName, String profileImageName,
            boolean isFollowed, String lastActiveDate, Review recentGame) {
        super(userId, firstName, lastName, profileImageName);
        this.isFollowed = isFollowed;
        this.lastActiveDate = lastActiveDate;
        this.recentGame = recentGame;
    }

    /**
     * Constructs a FollowableUser with specified details and follow status.
     *
     * @param userId           the unique identifier of the user
     * @param firstName        the first name of the user
     * @param lastName         the last name of the user
     * @param isFollowed       the follow status of the user by the current session
     *                         user
     * @param lastActiveDate   the date and time that this user has last made a post.
     */
    public FollowableUser(String userId, String firstName, String lastName,
            boolean isFollowed, String lastActiveDate) {
        super(userId, firstName, lastName);
        this.isFollowed = isFollowed;
        this.lastActiveDate = lastActiveDate;
    }

    /**
     * Returns the follow status of the user.
     *
     * @return true if the user is followed by the current session user, false
     *         otherwise
     */
    public boolean isFollowed() {
        return isFollowed;
    }

    /**
     * Returns the last active date and time of the user.
     *
     * @return the date and time that this user has last made a post.
     */
    public String isLastActiveDate() {
        return lastActiveDate;
    }

    /**
     * Returns recent reviews from this user.
     * 
     * @return recent reviews from this user
     */
    public Review getRecentGame() {
        return recentGame;
    }

    /**
     * Sets recent reviews from this user.
     * 
     * @param recentGame recent reviews from this user
     */
    public void setRecentGame(Review recentGame) {
        this.recentGame = recentGame;
    }



}
