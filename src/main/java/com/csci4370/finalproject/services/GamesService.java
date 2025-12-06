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
                                    new ArrayList<>()));

                    Platform platform = new Platform(
                            rs.getString("platform"), rs.getString("publisher"), rs.getInt("year"),
                            rs.getDouble("na_sales"), rs.getDouble("eu_sales"), rs.getDouble("jp_sales"),
                            rs.getDouble("other_sales"), rs.getDouble("global_sales"));
                    map.get(id).getPlatforms().add(platform);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(map.values());

    }

    // Get a list of the most popular games worldwide based on sales
    @Autowired
    public List<GameWithPlatforms> getMostPopularGlobal() {
        // Map<String, GameWithPlatforms> map = new HashMap<>();
        List<GameWithPlatforms> list = new ArrayList<>();

        final String sql = "SELECT" +
                "    g.game_id," +
                "    g.name," +
                "    g.genre," +
                "    GROUP_CONCAT(DISTINCT p.platform ORDER BY p.platform) AS platforms," +
                "    GROUP_CONCAT(DISTINCT p.publisher ORDER BY p.publisher) AS publishers," +
                "    GROUP_CONCAT(DISTINCT p.year ORDER BY p.year) AS years," +
                "    SUM(p.na_sales) AS total_na_sales," +
                "    SUM(p.eu_sales) AS total_eu_sales," +
                "    SUM(p.jp_sales) AS total_jp_sales," +
                "    SUM(p.other_sales) AS total_other_sales," +
                "    SUM(p.global_sales) AS total_global_sales" +
                "FROM games g" + //
                "JOIN platforms p ON g.game_id = p.game_id" +
                "GROUP BY g.game_id, g.name, g.genre;" +
                "ORDER BY total_global_sales DESC LIMIT 10;";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    GameWithPlatforms game = new GameWithPlatforms(
                            rs.getString("game_id"),
                            rs.getString("name"),
                            rs.getString("genre"),
                            new ArrayList<>());
                    int earliestYear = 0;
                    String yearsConcat = rs.getString("years");
                    if (yearsConcat != null && !yearsConcat.isEmpty()) {
                        String[] split = yearsConcat.split(",");
                        earliestYear = Integer.parseInt(split[0]);
                    }
                    Platform summary = new Platform(
                            rs.getString("platforms"), 
                            rs.getString("publishers"), 
                            earliestYear, 
                            rs.getDouble("na_sales"),
                            rs.getDouble("eu_sales"),
                            rs.getDouble("jp_sales"),
                            rs.getDouble("other_sales"),
                            rs.getDouble("global_sales"));
                    game.getPlatforms().add(summary);
                    list.add(game);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}
