package com.csci4370.finalproject.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {
   
   @Autowired
   private final DataSource dataSource;
   public FollowService(DataSource dataSource) {
      this.dataSource = dataSource;
   }

   public void followUser(String currentUser, String userIdToFollow) {
      String sql = "INSERT INTO follows (followingId, followedId) VALUES (?, ?)";
      try (Connection conn = dataSource.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, currentUser);
            pstmt.setString(2, userIdToFollow);
            pstmt.executeUpdate();
         } catch (SQLException e) {
            e.printStackTrace();
         }   
   }

   public void unfollowUser(String currentUser, String userIdToUnfollow) {
      String sql = "DELETE FROM follows WHERE followingId = ? AND followedId = ?";
      try (Connection conn = dataSource.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, currentUser);
            pstmt.setString(2, userIdToUnfollow);
            pstmt.executeUpdate();
         } catch (SQLException e) {
            e.printStackTrace();
         }   
   }
}