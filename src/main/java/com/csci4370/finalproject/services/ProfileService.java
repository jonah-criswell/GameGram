package com.csci4370.finalproject.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.csci4370.finalproject.models.Review;
import com.csci4370.finalproject.models.User;
import com.csci4370.finalproject.services.GamesService;

@Service
@SessionScope
public class ProfileService {
    
     private final DataSource dataSource;
    private final GamesService gameService;

     @Autowired
     public ProfileService(DataSource dataSource, GamesService gameService) {
        this.dataSource = dataSource;
        this.gameService = gameService;
     }

     public List<Review> getUserReviews(String userID) {
         // SQL: Selects review details, user details, and the game name.
         // formatting the date inside SQL makes Java handling easier.
         final String sql = "SELECT " +
                 "r.reviewId, r.content, DATE_FORMAT(r.postDate, '%M %d, %Y') as formattedDate, " +
                 "r.game_id, r.hoursPlayed, r.reviewRating, r.heartsCount, r.commentsCount, " +
                 "r.isHearted, r.isBookmarked, " +
                 "u.userId, u.firstName, u.lastName, " +
                 "g.name as gameName " +
                 "FROM review r " +
                 "JOIN user u ON r.userId = u.userId " +
                 "JOIN games g ON r.game_id = g.game_id " +
                 "WHERE u.userId = ? " +
                 "ORDER BY r.postDate DESC;";
         
         List<Review> reviews = new ArrayList<>();
         
         try (Connection conn = dataSource.getConnection();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {
              
             pstmt.setString(1, userID);
             
             try (ResultSet rs = pstmt.executeQuery()) {

                 while (rs.next()) {
                     String userId = rs.getString("userId");
                     String firstName = rs.getString("firstName");
                     String lastName = rs.getString("lastName");
                
                     User user = new User(userId, firstName, lastName);

                     // Review Info
                     String reviewId = String.valueOf(rs.getInt("reviewId"));
                     String content = rs.getString("content");
                     String postDate = rs.getString("formattedDate");
                     
                     int gameId = rs.getInt("game_id"); 

                     int hoursPlayed = rs.getInt("hoursPlayed");
                     int reviewRating = rs.getInt("reviewRating");
                     int heartsCount = rs.getInt("heartsCount");
                     int commentsCount = rs.getInt("commentsCount");
                     
                     // Bool fields
                     boolean isHearted = rs.getBoolean("isHearted");
                     boolean isBookmarked = rs.getBoolean("isBookmarked");

                     reviews.add(new Review(
                         reviewId, content, postDate, user, 
                         gameId, hoursPlayed, reviewRating, gameService.getGameById(gameId), 
                         heartsCount, commentsCount, 
                         isHearted, isBookmarked
                     ));
                 }
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }

         return reviews;
     }
}