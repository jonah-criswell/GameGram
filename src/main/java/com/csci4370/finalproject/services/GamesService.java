package com.csci4370.finalproject.services;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csci4370.finalproject.dto.GameWithPlatforms;
import com.csci4370.finalproject.models.Platform;

@Service
public class GamesService {

   private final DataSource dataSource;

   @Autowired
   public GamesService(DataSource dataSource) {
      this.dataSource = dataSource;
   }

   public List<GameWithPlatforms> searchGameByTitle(String name) {
    Map<String, GameWithPlatforms> map = new HashMap<>();

    final String sql = """
        SELECT g.game_id, g.name, g.genre, p.platform, p.year, p.publisher, p.na_sales, p.eu_sales, p.jp_sales, p.other_sales, p.global_sales
        FROM games g
        JOIN platforms p ON p.game_id = g.game_id
        WHERE g.name LIKE ?
    """;

    try (Connection conn = dataSource.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, "%" + name + "%");

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("game_id");

                map.putIfAbsent(id,
                    new GameWithPlatforms(
                        id,
                        rs.getString("name"),
                        rs.getString("genre"),
                        new ArrayList<>()
                    )
                );

                Platform platform = new Platform(
                    rs.getString("platform"), rs.getString("publisher"), rs.getInt("year"), rs.getDouble("na_sales"), rs.getDouble("eu_sales"), rs.getDouble("jp_sales"), rs.getDouble("other_sales"), rs.getDouble("global_sales")
                );
                map.get(id).getPlatforms().add(platform);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return new ArrayList<>(map.values());
}
}
