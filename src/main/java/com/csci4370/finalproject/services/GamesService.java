package com.csci4370.finalproject.services;

import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csci4370.finalproject.models.Game;

@Service
public class GamesService {

   private final DataSource dataSource;

   @Autowired
   public GamesService(DataSource dataSource) {
      this.dataSource = dataSource;
   }

   public List<Game> searchGameByTitle(String name) {
      List<Game> games = new ArrayList<>();
      String sql;
      
      if (name == null || name.isEmpty()) {
         // If no search query, return all games
         sql = "select * from games";
      } else {
         sql = "select * from games where name like ?";
      }
      
      try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

         if (name != null && !name.isEmpty()) {
            pstmt.setString(1, "%" + name + "%");
         }

         try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
               String gameId = rs.getString("game_id");
               String gameTitle = rs.getString("name");
               int year = rs.getInt("year");
               String genre = rs.getString("genre");
               String platform = rs.getString("platform");
               String publisher = rs.getString("publisher");
               games.add(new Game(gameId, gameTitle, year, platform, genre, publisher));
            }
         } catch (SQLException e) {
            e.printStackTrace();
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return games;
   }

   // Get a list of the most popular games worldwide based on sales
   public List<Game> getMostPopularGlobal() {
      List<Game> games = new ArrayList<>();
      final String sql = """
    SELECT g.game_id, g.name, g.year, g.genre, g.platform, g.publisher, p.global_sales
    FROM games g
    JOIN platforms p ON g.platform = p.platform_name
    ORDER BY p.global_sales DESC
    LIMIT 10
""";

      try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {
            
         while (rs.next()) {
            String gameId = rs.getString("game_id");
            String gameTitle = rs.getString("name");
            int year = rs.getInt("year");
            String genre = rs.getString("genre");
            String platform = rs.getString("platform");
            String publisher = rs.getString("publisher");
            games.add(new Game(gameId, gameTitle, year, platform, genre, publisher));
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return games;
   }
}
