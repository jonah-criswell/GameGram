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

import com.csci4370.finalproject.models.FollowableUser;
import com.csci4370.finalproject.models.User;
/**
 * This service contains people related functions.
 */
@Service
public class PeopleService {
    
    /**
     * This function should query and return all users that 
     * are followable. The list should not contain the user 
     * with id userIdToExclude.
     */
    @Autowired
    private final DataSource dataSource;
    public PeopleService(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public List<FollowableUser> getFollowableUsers(String userIdToExclude) {
        List<FollowableUser> followableUsers = new ArrayList<>();
        
        String sql = "SELECT * FROM user WHERE userId != ?";
       try (Connection conn = dataSource.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userIdToExclude);
        
            try(ResultSet rs = pstmt.executeQuery()) {
                
                while (rs.next()) {
                    String user_id = rs.getString(1);
                    //String username = rs.getString(1);
                    String firstname = rs.getString(4);
                    String lastname = rs.getString(5);

                    String isFollowSQL = "SELECT 1 FROM follows WHERE followingId = ? AND followedId = ? LIMIT 1";
                    String getLastPostDateSQL = "SELECT DATE_FORMAT(postDate, '%b %d, %Y, %h:%i %p') AS formattedDate FROM review WHERE userId = ? ORDER BY postDate DESC LIMIT 1";
                    boolean isFollow = false;
                    try (PreparedStatement pstmt2 = conn.prepareStatement(isFollowSQL)) {
                        pstmt2.setString(1, userIdToExclude);
                        pstmt2.setString(2, user_id);
                        try (ResultSet rs2 = pstmt2.executeQuery()) {
                            isFollow = rs2.next(); 
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try (PreparedStatement pstmt3 = conn.prepareStatement(getLastPostDateSQL)) {
                        pstmt3.setString(1, user_id);
                        try (ResultSet rs3 = pstmt3.executeQuery()) {
                            if (rs3.next()) {
                                String lastPostDate = rs3.getString(1);
                                followableUsers.add(new FollowableUser(user_id, firstname, lastname, isFollow, lastPostDate));
                            } else {
                                followableUsers.add(new FollowableUser(user_id, firstname, lastname, isFollow, "No Posts"));
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }   

         } catch (SQLException e) {
            e.printStackTrace();
         }

        return followableUsers;
    }

    public List<User> getAllOtherUsers(String userIdToExclude) {
        List<User> allOtherUsers = new ArrayList<>();
        String sql = "SELECT * FROM user WHERE userId != ?";
       try (Connection conn = dataSource.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userIdToExclude);
        
            try(ResultSet rs = pstmt.executeQuery()) {
                
                while (rs.next()) {
                    String user_id = rs.getString(1);
                    //String username = rs.getString(1);
                    String firstname = rs.getString(4);
                    String lastname = rs.getString(5);

                    allOtherUsers.add(new User(user_id, firstname, lastname, ""));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }   

         } catch (SQLException e) {
            e.printStackTrace();
         }

       
        return allOtherUsers;
    }

    /**
     * Retrieves ONLY the users that the current user is following.
     * This is lightweight because the Controller handles fetching the full Game Review later.
     */
    public List<FollowableUser> getFollowedUsers(String currentUserId) {
        List<FollowableUser> followedUsers = new ArrayList<>();

       
        String sql = "SELECT u.userId, u.firstName, u.lastName, " +
                     "DATE_FORMAT(MAX(r.postDate), '%b %d, %Y') as lastActiveDate " +
                     "FROM user u " +
                     "JOIN follows f ON u.userId = f.followedId " +
                     "LEFT JOIN review r ON u.userId = r.userId " +
                     "WHERE f.followingId = ? " +
                     "GROUP BY u.userId, u.firstName, u.lastName";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, currentUserId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String userId = rs.getString("userId");
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");
                    String lastActiveDate = rs.getString("lastActiveDate");

                    boolean isFollow = true;

                    if (lastActiveDate == null) {
                        lastActiveDate = "No recent activity";
                    }

                    
                    followedUsers.add(new FollowableUser(userId, firstName, lastName, isFollow, lastActiveDate));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return followedUsers;
    }
}