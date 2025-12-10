package com.csci4370.finalproject.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.csci4370.finalproject.dto.GameWithPlatforms;
import com.csci4370.finalproject.models.Review;
import com.csci4370.finalproject.services.GamesService;
import com.csci4370.finalproject.services.ReviewService;

@Controller
@RequestMapping("/games")
public class GameSearchController {

      private final GamesService gamesService;
      private final ReviewService reviewService;

      @Autowired
      public GameSearchController(GamesService gamesService, ReviewService reviewService) {
         this.gamesService = gamesService;
         this.reviewService = reviewService;
      }

      @GetMapping
      public ModelAndView webpage(@RequestParam(name = "query", required = false)  String query) {
         ModelAndView mv = new ModelAndView("games_page");
         List<GameWithPlatforms> games = gamesService.searchGameByTitle(query);
         mv.addObject("games", games);
         return mv;
      }

      @GetMapping("/{gameId}")
      public ModelAndView gameDetails(@PathVariable String gameId) {

         // Retrieve game details and reviews for the specified gameId
         ModelAndView mv = new ModelAndView("game_details_page");
         List<Review> reviews = reviewService.getReviewsByGameId(gameId);
         mv.addObject("reviews", reviews);

         String gameName = gamesService.getGameById(Integer.parseInt(gameId));
         mv.addObject("gameName", gameName);

         return mv;
      }

      @GetMapping("/{gameId}/reviews/{postId}")
      public ModelAndView reviewDetails(@PathVariable String gameId,
                                       @PathVariable String postId) {

         // Retrieve review details and comments for the specified postId
         ModelAndView mv = new ModelAndView("reviews_details_page");
         Review review = reviewService.getReviewByPostId(postId);
         mv.addObject("review", review);
         mv.addObject("gameId", gameId);
         mv.addObject("comments", reviewService.getCommentsByReviewId(postId));

         return mv;
      }



}
