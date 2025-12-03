package com.csci4370.finalproject.services;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.csci4370.finalproject.models.User;
import com.csci4370.finalproject.models.Review;

@Service
@SessionScope
public class ProfileService {
    
    // private final DataSource dataSource;

    // @Autowired
    // public UserService(DataSource dataSource) {
    //     this.dataSource = dataSource;
    //     this.passwordEncoder = new BCryptPasswordEncoder();
    // }

    // public List<Post> getUserReviews(String userID) {
    //     final String sql = "SELECT * FROM user u LEFT JOIN reviews r on u.userId = r.review_by WHERE u.userId = ?;";
    //     List<Post> reviews = new ArrayList<>();
    //     try (Connection conn = dataSource.getConnection();
    //             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    //         pstmt.setString(1, userID);
    //         try (ResultSet rs = pstmt.executeQuery()) {

    //             while (rs.next()) {

    //                 String userId = rs.getString("userId");
    //                 String username = rs.getString("username");
    //                 String firstName = rs.getString("firstName");
    //                 String lastName = rs.getString("lastName");
    //                 //String profileImagePath = rs.getString("profileImagePath");

    //                 String postId = rs.getString("postId");
    //                 String content = rs.getString("content");
    //                 String postDate = rs.getString("formattedDate");
    //                 //User user = new User(userId, firstName, lastName, profileImagePath);
    //                 User user = new User(userId, firstName, lastName);
    //                 // For more complicated post:
    //                 int heartsCount = rs.getInt("heartsCount");
    //                 int commentsCount = rs.getInt("commentsCount");
    //                 //boolean isHearted = rs.getBoolean("isHearted");
    //                 boolean isHeartedBoolean;
    //                 final String checkHeartSql = "SELECT * FROM post_hearts WHERE userId = ? AND postId = ?;";
    //                 try (PreparedStatement checkHeartStmt = conn.prepareStatement(checkHeartSql)) {
    //                     checkHeartStmt.setString(1, userID);
    //                     checkHeartStmt.setString(2, postId);
    //                     try (ResultSet heartRs = checkHeartStmt.executeQuery()) {
    //                         isHeartedBoolean = heartRs.next();
    //                     }
    //                 }
    //                 //boolean isBookmarked = rs.getBoolean("isBookmarked");
    //                 boolean isBookmarkedBoolean;
    //                 final String checkBookmarkSql = "SELECT * FROM post_bookmarks WHERE userId = ? AND postId = ?;";
    //                 try (PreparedStatement checkBookmarkStmt = conn.prepareStatement(checkBookmarkSql)) {
    //                     checkBookmarkStmt.setString(1, userID);
    //                     checkBookmarkStmt.setString(2, postId);
    //                     try (ResultSet bookmarkRs = checkBookmarkStmt.executeQuery()) {
    //                         isBookmarkedBoolean = bookmarkRs.next();
    //                     }
    //                 }
    //                 reviews.add(new Review(postId, content, postDate, user, heartsCount, commentsCount, isHeartedBoolean, isBookmarkedBoolean));
    //                // posts.add(new BasicPost(postId, content, postDate, user)); //TODO: make tables and calculate heartsCount, commentsCount, isHearted, isBookmarked
    //             }
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }

    //     return reviews;
    // }



}
