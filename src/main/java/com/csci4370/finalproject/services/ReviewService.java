
package com.csci4370.finalproject.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.csci4370.finalproject.models.Comment;
import com.csci4370.finalproject.models.Review;
import com.csci4370.finalproject.models.User;

@Service
@SessionScope
public class ReviewService {

    // dataSource enables talking to the database.
    private final DataSource dataSource;
    // userService provides user-related operations within this session.
    private final UserService userService;

    private final GamesService gamesService;
    // passwordEncoder is used for password security.
    private final BCryptPasswordEncoder passwordEncoder;
    // This holds
    // private User loggedInUser = null;

    /**
     * See AuthInterceptor notes regarding dependency injection and
     * inversion of control.
     */
    @Autowired
    public ReviewService(DataSource dataSource, UserService userService, GamesService gamesService) {
        this.dataSource = dataSource;
        this.userService = userService;
        this.gamesService = gamesService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean makeReview(int hoursPlayed, int game_id, String content, int reviewRating, User user) {
        // Note the ? marks in the SQL statement. They are placeholders like mentioned above.
        final String postSql = "insert into review (game_id, hoursPlayed, content, reviewRating, postDate, userId) values (?, ?, ?, ?, now(), ?)";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement postStmt = conn.prepareStatement(postSql)) {
            postStmt.setInt(1, game_id);
            postStmt.setInt(2, hoursPlayed);
            postStmt.setString(3, content);
            postStmt.setInt(4, reviewRating);
            postStmt.setString(5, user.getUserId());
            int rowsAffected = postStmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Review> getReviewsByGameId(String gameId) {
        List<Review> reviews = new ArrayList<>();
        final String reviewSql = "SELECT * FROM review WHERE game_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement reviewStmt = conn.prepareStatement(reviewSql)) {
            reviewStmt.setInt(1, Integer.parseInt(gameId));
            ResultSet rs = reviewStmt.executeQuery();
            while (rs.next()) {
                String reviewId = rs.getString("reviewId");
                int hoursPlayed = rs.getInt("hoursPlayed");
                String content = rs.getString("content");
                int reviewRating = rs.getInt("reviewRating");
                String reviewDate = rs.getString("postDate");
                String userId = rs.getString("userId");
                int gameIdInt = rs.getInt("game_id");
                String gameName = gamesService.getGameById(gameIdInt);
                int heartsCount = rs.getInt("heartsCount");
                int commentsCount = rs.getInt("commentsCount");
                boolean isHearted = rs.getBoolean("isHearted");

                Review review = new Review(
                    reviewId,
                    content,
                    reviewDate,
                    userService.getUserById(userId),
                    gameIdInt,
                    hoursPlayed,
                    reviewRating,
                    gameName,
                    heartsCount,
                    commentsCount,
                    isHearted,
                    false
                );

                reviews.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public Review getRecentReviewsFromFollowedUsers(String userId) {
    Review review = null;
    final String reviewSql = """
        SELECT r.*, g.name as gameName
        FROM review r
        JOIN games g ON r.game_id = g.game_id
        WHERE r.userId = ?
        ORDER BY r.postDate DESC
        LIMIT 1
    """;

    try (Connection conn = dataSource.getConnection();
         PreparedStatement reviewStmt = conn.prepareStatement(reviewSql)) {

        reviewStmt.setString(1, userId);
        try (ResultSet rs = reviewStmt.executeQuery()) {
            if (rs.next()) { // <-- check if there’s a row before reading
                String reviewId = rs.getString("reviewId");
                int hoursPlayed = rs.getInt("hoursPlayed");
                String content = rs.getString("content");
                int reviewRating = rs.getInt("reviewRating");
                String reviewDate = rs.getString("postDate");
                int gameId = rs.getInt("game_id");
                String reviewUserId = rs.getString("userId");
                //int gameIdInt = gameId.parseInt(); // from your review query
                String gameName = gamesService.getGameById(gameId); // returns game name

                review = new Review(
                    reviewId,
                    content,
                    reviewDate,
                    userService.getUserById(reviewUserId),
                    gameId,
                    hoursPlayed,
                    reviewRating,
                    gameName,  // <- string for the constructor
                    0, // heartsCount
                    0, // commentsCount
                    false, // isHearted
                    false  // isBookmarked
                );

            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return review; // will be null if no recent review exists
}

    public Review getReviewByPostId(String reviewId){
        Review review = null;
        final String reviewSql = "SELECT * FROM review WHERE reviewId = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement reviewStmt = conn.prepareStatement(reviewSql)) {
            reviewStmt.setString(1, reviewId);
            ResultSet rs = reviewStmt.executeQuery();
            if (rs.next()) {
                int hoursPlayed = rs.getInt("hoursPlayed");
                String content = rs.getString("content");
                int reviewRating = rs.getInt("reviewRating");
                String reviewDate = rs.getString("postDate");
                String userId = rs.getString("userId");
                int gameIdInt = rs.getInt("game_id");
                String gameName = gamesService.getGameById(gameIdInt);
                int heartsCount = rs.getInt("heartsCount");
                int commentsCount = rs.getInt("commentsCount");
                boolean isHearted = rs.getBoolean("isHearted");


                review = new Review(
                    reviewId,
                    content,
                    reviewDate,
                    userService.getUserById(userId),
                    gameIdInt,
                    hoursPlayed,
                    reviewRating,
                    gameName,
                    heartsCount,
                    commentsCount,
                    isHearted,
                    false
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return review;
    }

    public boolean hasUserHearted(String reviewId, User user) throws SQLException {
        final String sql = "SELECT 1 FROM review_hearts WHERE reviewId = ? AND userId = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reviewId);
            stmt.setString(2, user.getUserId());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

     public boolean addOrRemoveHeart(String reviewId, User user, boolean isAdd) throws SQLException {
        boolean testBoolean = false;
        if (isAdd) {
            final String updateHeartSql = "insert into review_hearts (reviewId, userId) values (?, ?)";
            try (Connection conn = dataSource.getConnection();
                    PreparedStatement updateHeartStmt = conn.prepareStatement(updateHeartSql)) {
                updateHeartStmt.setString(1, reviewId);
                updateHeartStmt.setString(2, user.getUserId());
                int rowsAffected = updateHeartStmt.executeUpdate();
                testBoolean = rowsAffected > 0;
    }

        } else {
            final String deleteBookmarkSql = "delete from review_hearts where reviewId = ? and userId = ?";
            try (Connection conn = dataSource.getConnection();
                    PreparedStatement deleteBookmarkStmt = conn.prepareStatement(deleteBookmarkSql)) {
                deleteBookmarkStmt.setString(1, reviewId);
                deleteBookmarkStmt.setString(2, user.getUserId());
                int rowsAffected = deleteBookmarkStmt.executeUpdate();
                testBoolean = rowsAffected > 0;
    }
    }
    // update the heart count here
        final String updateHeartCountSql = "update review set heartsCount = (" +
                "SELECT COUNT(userId) as heart_count FROM review_hearts WHERE reviewId = ?) " +
                "WHERE reviewId = ?";
        try (Connection conn = dataSource.getConnection();
        PreparedStatement updateHeartsCountStmt = conn.prepareStatement(updateHeartCountSql)) {
            updateHeartsCountStmt.setString(1, reviewId);
            updateHeartsCountStmt.setString(2, reviewId);
            int rowsAffectedCountUpdate = updateHeartsCountStmt.executeUpdate();
            testBoolean = rowsAffectedCountUpdate > 0;
        }

        return testBoolean;

    }

    public int getHeartCount(String reviewId) {
        int count = 0;
        
        final String updateHeartCountSql = "update review set heartsCount = (" +
                "SELECT COUNT(userId) as heart_count FROM review_hearts WHERE reviewId = ?) " +
                "WHERE reviewId = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement updateHeartsCountStmt = conn.prepareStatement(updateHeartCountSql)) {
            updateHeartsCountStmt.setString(1, reviewId);
            updateHeartsCountStmt.setString(2, reviewId);
            int rowsAffectedCountUpdate = updateHeartsCountStmt.executeUpdate();
    } catch (SQLException e) {
    e.printStackTrace();
    }
        final String accessHeartCountSql = "SELECT heartsCount FROM review WHERE reviewId = ?";
    try (Connection conn = dataSource.getConnection();
                PreparedStatement accessHeartsCountStmt = conn.prepareStatement(accessHeartCountSql)) {
            accessHeartsCountStmt.setString(1, reviewId);
            ResultSet rs = accessHeartsCountStmt.executeQuery();
            count = rs.getInt("heartsCount");
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return count;

    }



    // // Search posts containing all hashtags in the list, seperated by spaces.
    // // TODO: prevent empty searches, fix things like #hashtags being returned for #hashtag. The search should not be returning partial matches.
    // public List<Review> searchReviews(List<String> hashtags) {
    //     List<Review> posts = new ArrayList<>();
    //     StringBuilder searchSql = new StringBuilder("SELECT postId FROM full_post WHERE ");
    //     for (int i = 0; i < hashtags.size(); i++) {
    //         searchSql.append("content REGEXP ?");
    //         if (i < hashtags.size() - 1) {
    //             searchSql.append(" AND ");
    //         }
    //     }
    //     searchSql.append(" ORDER BY postDate DESC;");

    //     boolean testBoolean = false;

    //     if (isAdd) {

    //         final String updateHeartSql = "insert into review_hearts (reviewId, userId) values (?, ?)";
    //         try (Connection conn = dataSource.getConnection();
    //                 PreparedStatement updateHeartStmt = conn.prepareStatement(updateHeartSql)) {
    //             updateHeartStmt.setString(1, reviewId);
    //             updateHeartStmt.setString(2, user.getUserId());
    //             int rowsAffected = updateHeartStmt.executeUpdate();
    //             testBoolean = rowsAffected > 0;
    //         }

    //     } else {
    //         final String deleteBookmarkSql = "delete from review_hearts where reviewId = ? and userId = ?";
    //         try (Connection conn = dataSource.getConnection();
    //                 PreparedStatement deleteBookmarkStmt = conn.prepareStatement(deleteBookmarkSql)) {
    //             deleteBookmarkStmt.setString(1, reviewId);
    //             deleteBookmarkStmt.setString(2, user.getUserId());
    //             int rowsAffected = deleteBookmarkStmt.executeUpdate();
    //             testBoolean = rowsAffected > 0;
    //         }
    //     }

    //     // update the heart count here
    //     final String updateHeartCountSql = "update review set heartsCount = (" +
    //             "SELECT COUNT(userId) as heart_count FROM review_hearts WHERE reviewId = ?) " +
    //             "WHERE reviewId = ?";
    //     try (Connection conn = dataSource.getConnection();
    //             PreparedStatement updateHeartsCountStmt = conn.prepareStatement(updateHeartCountSql)) {
    //         updateHeartsCountStmt.setString(1, reviewId);
    //         updateHeartsCountStmt.setString(2, reviewId);
    //         int rowsAffectedCountUpdate = updateHeartsCountStmt.executeUpdate();
    //         testBoolean = rowsAffectedCountUpdate > 0;
    //     }

    //     return testBoolean;

    // }


    public boolean addComment(String reviewId, User user, String comment) throws SQLException {

        boolean testBoolean = false;

        final String insertCommentSql = "INSERT INTO review_comments (commentText, commentDate, reviewId, userId) VALUES (?, now(), ?, ?)";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement insertCommentStmt = conn.prepareStatement(insertCommentSql)) {
            insertCommentStmt.setString(1, comment);
            insertCommentStmt.setString(2, reviewId);
            insertCommentStmt.setString(3, user.getUserId());
            int rowsAffected = insertCommentStmt.executeUpdate();
            testBoolean = rowsAffected > 0;
        }
        final String updateCommentsSql = "update review set commentsCount = (" +
                "SELECT COUNT(userId) as comment_count FROM review_comments WHERE reviewId = ?) " +
                "WHERE reviewId = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement updateCommentsStmt = conn.prepareStatement(updateCommentsSql)) {
            updateCommentsStmt.setString(1, reviewId);
            updateCommentsStmt.setString(2, reviewId);
            int rowsAffectedCountUpdate = updateCommentsStmt.executeUpdate();
            testBoolean = rowsAffectedCountUpdate > 0;
        }

        return testBoolean;

    }

    public int getCommentCount(String reviewId) {
        int count = 0;
        
        final String updateCommentCountSql = "update review set commentsCount = (" +
                "SELECT COUNT(userId) as comment_count FROM review_comments WHERE reviewId = ?) " +
                "WHERE reviewId = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement updateCommentsCountStmt = conn.prepareStatement(updateCommentCountSql)) {
            updateCommentsCountStmt.setString(1, reviewId);
            updateCommentsCountStmt.setString(2, reviewId);
            int rowsAffectedCountUpdate = updateCommentsCountStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final String accessHeartCountSql = "SELECT commentsCount FROM review WHERE reviewId = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement accessHeartsCountStmt = conn.prepareStatement(accessHeartCountSql)) {
            accessHeartsCountStmt.setString(1, reviewId);
            ResultSet rs = accessHeartsCountStmt.executeQuery();
            count = rs.getInt("commentsCount");
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return count;

    }

    // Search posts containing all hashtags in the list, seperated by spaces.
    // TODO: prevent empty searches, fix things like #hashtags being returned for
    // #hashtag. The search should not be returning partial matches.
    // public List<Review> searchReviews(List<String> hashtags) {
    // List<Review> posts = new ArrayList<>();
    // StringBuilder searchSql = new StringBuilder("SELECT postId FROM full_post
    // WHERE ");
    // for (int i = 0; i < hashtags.size(); i++) {
    // searchSql.append("content REGEXP ?");
    // if (i < hashtags.size() - 1) {
    // searchSql.append(" AND ");
    // }
    // }
    // searchSql.append(" ORDER BY postDate DESC;");

    // try (Connection conn = dataSource.getConnection();
    // PreparedStatement searchStmt = conn.prepareStatement(searchSql.toString())) {
    // for (int i = 0; i < hashtags.size(); i++) {
    // String tag = hashtags.get(i).startsWith("#") ? hashtags.get(i).substring(1) :
    // hashtags.get(i);
    // searchStmt.setString(i + 1, "(^|[^#A-Za-z0-9_])#" + tag +
    // "($|[^A-Za-z0-9_])");
    // }
    // ResultSet rs = searchStmt.executeQuery();
    // while (rs.next()) {
    // posts.add(getPostById(rs.getString("postId")));
    // }
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // return posts;
    // }

    // public Review getReviewById(String postId) {
    // Review review = null;
    // Review review2 = null;
    // String postSql = "SELECT *, DATE_FORMAT(postDate, '%b %d, %Y, %h:%i %p') AS
    // formattedDate FROM review WHERE reviewId = ?";
    // try (Connection conn = dataSource.getConnection();
    // PreparedStatement postStmt = conn.prepareStatement(postSql)) {
    // postStmt.setString(1, postId);
    // ResultSet rs = postStmt.executeQuery();
    // if (rs.next()) {
    // String content = rs.getString("content");
    // String postDate = rs.getString("formattedDate");
    // int heartsCount = rs.getInt("heartsCount");
    // int commentsCount = rs.getInt("commentsCount");
    // // int isHearted = rs.getInt("isHearted");
    // boolean isHeartedBoolean;
    // String userIdFromPost = userService.getLoggedInUser().getUserId();;
    // final String checkHeartSql = "SELECT * FROM post_hearts WHERE userId = ? AND
    // postId = ?;";
    // try (PreparedStatement checkHeartStmt = conn.prepareStatement(checkHeartSql))
    // {
    // checkHeartStmt.setString(1, userIdFromPost);
    // checkHeartStmt.setString(2, postId);
    // try (ResultSet heartRs = checkHeartStmt.executeQuery()) {
    // isHeartedBoolean = heartRs.next();
    // }
    // }

    // // Fetch comments for the post
    // List<Comment> comments = new ArrayList<>();
    // String commentsSql = "SELECT *, DATE_FORMAT(commentDate, '%b %d, %Y, %h:%i
    // %p') AS formattedDate FROM post_comments WHERE postId = ? ORDER BY
    // commentDate ASC;";
    // try (PreparedStatement commentsStmt = conn.prepareStatement(commentsSql)) {
    // commentsStmt.setString(1, postId);
    // ResultSet commentsRs = commentsStmt.executeQuery();
    // while (commentsRs.next()) {
    // String commentId = commentsRs.getString("commentId");
    // String commentText = commentsRs.getString("commentText");
    // String commentDate = commentsRs.getString("formattedDate");
    // String commentUserId = commentsRs.getString("userId");

    // // Fetch user details for the comment
    // String userSQL = "SELECT * FROM user WHERE userId = ?";
    // try (Connection userConn = dataSource.getConnection();
    // PreparedStatement userStmt = userConn.prepareStatement(userSQL)) {
    // userStmt.setString(1, commentUserId);
    // ResultSet userRs = userStmt.executeQuery();
    // if (userRs.next()) {
    // String firstName = userRs.getString("firstName");
    // String lastName = userRs.getString("lastName");
    // User commentUser = new User(commentUserId, firstName, lastName);
    // Comment comment = new Comment(commentId, commentText, commentDate,
    // commentUser);
    // comments.add(comment);
    // }
    // }
    // }
    // }

    // // int isBookmarked = rs.getInt("isBookmarked");
    // boolean isBookmarkedBoolean;
    // final String checkBookmarkSql = "SELECT * FROM post_bookmarks WHERE userId =
    // ? AND postId = ?;";
    // try (PreparedStatement checkBookmarkStmt =
    // conn.prepareStatement(checkBookmarkSql)) {
    // checkBookmarkStmt.setString(1, userIdFromPost);
    // checkBookmarkStmt.setString(2, postId);
    // try (ResultSet bookmarkRs = checkBookmarkStmt.executeQuery()) {
    // isBookmarkedBoolean = bookmarkRs.next();
    // }
    // }

    // String userSQL = "SELECT * FROM user WHERE userId = ?";
    // try (Connection userConn = dataSource.getConnection();
    // PreparedStatement userStmt = userConn.prepareStatement(userSQL)) {
    // userStmt.setString(1, rs.getString("userId"));
    // ResultSet userRs = userStmt.executeQuery();
    // if (userRs.next()) {
    // String userId = userRs.getString("userId");
    // String firstName = userRs.getString("firstName");
    // String lastName = userRs.getString("lastName");
    // User user = new User(userId, firstName, lastName);
    // post = new ExpandedPost(postId, content, postDate, user, heartsCount,
    // commentsCount, isHeartedBoolean, isBookmarkedBoolean, comments);
    // //TODO: Calculate heartsCount, commentsCount, isHearted, isBookmarked
    // }
    // }
    // }
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // return (ExpandedPost) post;
    // }

    // public boolean addOrRemoveHeart(String postId, User user, boolean isAdd)
    // throws SQLException {

    // if (isAdd) {
    // final String insertBookmarkSql = "insert into post_hearts (postId, userId)
    // values (?, ?)";
    // final String updateHeartsSql = "update full_post set heartsCount =
    // heartsCount + 1 where postId = ?";
    // try (Connection conn = dataSource.getConnection();
    // PreparedStatement updateHeartsStmt = conn.prepareStatement(updateHeartsSql))
    // {
    // updateHeartsStmt.setString(1, postId);
    // int rowsAffectedCountUpdate = updateHeartsStmt.executeUpdate();
    // }

    // try (Connection conn = dataSource.getConnection();
    // PreparedStatement insertBookmarkStmt =
    // conn.prepareStatement(insertBookmarkSql)) {
    // insertBookmarkStmt.setString(1, postId);
    // insertBookmarkStmt.setString(2, user.getUserId());
    // int rowsAffected = insertBookmarkStmt.executeUpdate();
    // return rowsAffected > 0;
    // }
    // } else {
    // final String deleteBookmarkSql = "delete from post_hearts where postId = ?
    // and userId = ?";
    // final String updateHeartsSql = "update full_post set heartsCount =
    // heartsCount - 1 where postId = ?";
    // try (Connection conn = dataSource.getConnection();
    // PreparedStatement updateHeartsStmt = conn.prepareStatement(updateHeartsSql))
    // {
    // updateHeartsStmt.setString(1, postId);
    // int rowsAffectedCountUpdate = updateHeartsStmt.executeUpdate();
    // }

    // try (Connection conn = dataSource.getConnection();
    // PreparedStatement deleteBookmarkStmt =
    // conn.prepareStatement(deleteBookmarkSql)) {
    // deleteBookmarkStmt.setString(1, postId);
    // deleteBookmarkStmt.setString(2, user.getUserId());
    // int rowsAffected = deleteBookmarkStmt.executeUpdate();
    // return rowsAffected > 0;
    // }
    // }
    // }

    // public boolean addComment(String postId, User user, String comment) throws
    // SQLException {
    // /**
    // * SQL COMMAND TO CREATE TABLE FOR COMMENTS
    // * create table if not exists post_comments (
    // * commentId int auto_increment,
    // * postId int not null,
    // * userId int not null,
    // * commentText varchar(255) not null,
    // * commentDate datetime not null,
    // * primary key (commentId),
    // * foreign key (userId) references user(userId),
    // * foreign key (postId) references post(postId)
    // * );
    // */

    // final String insertCommentSql = "insert into post_comments (postId, userId,
    // commentText, commentDate) values (?, ?, ?, now())";
    // final String updateCommentsSql = "update full_post set CommentsCount =
    // CommentsCount + 1 where postId = ?";
    // try (Connection conn = dataSource.getConnection();
    // PreparedStatement updateCommentsStmt =
    // conn.prepareStatement(updateCommentsSql))
    // { updateCommentsStmt.setString(1, postId);
    // int rowsAffectedCountUpdate = updateCommentsStmt.executeUpdate();
    // }
    // try (Connection conn = dataSource.getConnection();
    // PreparedStatement insertCommentStmt =
    // conn.prepareStatement(insertCommentSql)) {
    // insertCommentStmt.setString(1, postId);
    // insertCommentStmt.setString(2, user.getUserId());
    // insertCommentStmt.setString(3, comment);
    // int rowsAffected = insertCommentStmt.executeUpdate();
    // return rowsAffected > 0;
    // }
    // }

    // public boolean addComment(String postId, User user,  String comment) throws SQLException {
    //     /**
    //      * SQL COMMAND TO CREATE TABLE FOR COMMENTS
    //      * create table if not exists post_comments (
    //      *  commentId int auto_increment,
    //      *  postId int not null,
    //      *  userId int not null,
    //      *  commentText varchar(255) not null,
    //      *  commentDate datetime not null,
    //      *  primary key (commentId),
    //      *  foreign key (userId) references user(userId),
    //      *  foreign key (postId) references post(postId)
    //      *  );
    //      */

    //     final String insertCommentSql = "insert into post_comments (postId, userId, commentText, commentDate) values (?, ?, ?, now())";
    //     final String updateCommentsSql = "update full_post set CommentsCount = CommentsCount + 1 where postId = ?";
    //     try (Connection conn = dataSource.getConnection();
    //             PreparedStatement updateCommentsStmt = conn.prepareStatement(updateCommentsSql))
    // {            updateCommentsStmt.setString(1, postId); 
    //         int rowsAffectedCountUpdate = updateCommentsStmt.executeUpdate();
    // }
    //     try (Connection conn = dataSource.getConnection();
    //             PreparedStatement insertCommentStmt = conn.prepareStatement(insertCommentSql)) {
    //         insertCommentStmt.setString(1, postId);
    //         insertCommentStmt.setString(2, user.getUserId());
    //         insertCommentStmt.setString(3, comment);
    //         int rowsAffected = insertCommentStmt.executeUpdate();
    //         return rowsAffected > 0;
    //     }
    // }

    public List<Comment> getCommentsByReviewId(String reviewId) {
        List<Comment> comments = new ArrayList<>();
        final String commentsSql = "SELECT *, DATE_FORMAT(commentDate, '%b %d, %Y, %h:%i %p') AS formattedDate FROM review_comments WHERE reviewId = ? ORDER BY commentDate ASC";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement commentsStmt = conn.prepareStatement(commentsSql)) {
            commentsStmt.setString(1, reviewId);
            ResultSet commentsRs = commentsStmt.executeQuery();
            
            while (commentsRs.next()) {
                String commentId = commentsRs.getString("commentId");
                String commentText = commentsRs.getString("commentText");
                String commentDate = commentsRs.getString("formattedDate");
                String commentUserId = commentsRs.getString("userId");

                // Fetch user details for the comment
                User commentUser = userService.getUserById(commentUserId);
                Comment comment = new Comment(commentId, commentText, commentDate, commentUser);
                comments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return comments;
    }}

    // public boolean addOrRemoveBookmark(String postId, User user, boolean isAdd)
    // throws SQLException {
    // /**
    // * SQL COMMAND TO CREATE TABLE FOR BOOKMARKS
    // * create table if not exists post_bookmarks (
    // * userId int not null,
    // * postId int not null,
    // * primary key (userId, postId),
    // * foreign key (userId) references user(userId),
    // * foreign key (postId) references post(postId)
    // * );
    // */

    // if (isAdd) {
    // final String insertBookmarkSql = "insert into post_bookmarks (postId, userId)
    // values (?, ?)";
    // try (Connection conn = dataSource.getConnection();
    // PreparedStatement insertBookmarkStmt =
    // conn.prepareStatement(insertBookmarkSql)) {
    // insertBookmarkStmt.setString(1, postId);
    // insertBookmarkStmt.setString(2, user.getUserId());
    // int rowsAffected = insertBookmarkStmt.executeUpdate();
    // return rowsAffected > 0;
    // }
    // } else {
    // final String deleteBookmarkSql = "delete from post_bookmarks where postId = ?
    // and userId = ?";
    // try (Connection conn = dataSource.getConnection();
    // PreparedStatement deleteBookmarkStmt =
    // conn.prepareStatement(deleteBookmarkSql)) {
    // deleteBookmarkStmt.setString(1, postId);
    // deleteBookmarkStmt.setString(2, user.getUserId());
    // int rowsAffected = deleteBookmarkStmt.executeUpdate();
    // return rowsAffected > 0;
    // }
    // }
    // }

    // }

