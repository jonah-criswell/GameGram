package com.csci4370.finalproject.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.csci4370.finalproject.models.Game;
import com.csci4370.finalproject.services.GamesService;
import com.csci4370.finalproject.services.ReviewService;
import com.csci4370.finalproject.services.UserService;
import org.springframework.web.bind.annotation.RequestParam;
import java.net.URLEncoder;

@Controller
@RequestMapping("/games/{gameId}/makereview")
public class ReviewController {
   private final ReviewService reviewService;
   private final UserService userService;
   private final GamesService gamesService;

   @Autowired
   public ReviewController(ReviewService reviewService, UserService userService, GamesService gamesService) {
      this.reviewService = reviewService;
      this.userService = userService;
      this.gamesService = gamesService;
   }

   @GetMapping
   public ModelAndView showReviewPage(@PathVariable String gameId) {
      ModelAndView mv = new ModelAndView("make_review_page");
      mv.addObject("gameId", gameId);
      return mv;
   }

   @PostMapping
   public String makeReview(@PathVariable String gameId, @RequestParam("rating") int rating, @RequestParam("hours_played") int hoursPlayed, @RequestParam("content") String reviewText) {
      reviewService.makeReview(hoursPlayed, Integer.parseInt(gameId), reviewText, rating, userService.getLoggedInUser());
      return "redirect:/games/" + gameId;
   }

   @PostMapping("/comments/{postId}")
    public String postComment(@PathVariable("postId") String postId,
                              @RequestParam(name = "comment") String comment,
                              @RequestParam("gameId") String gameId) {
        System.out.println("The user is attempting add a comment:");
        System.out.println("\tpostId: " + postId);
        System.out.println("\tcomment: " + comment);
        try {
            reviewService.addComment(postId, userService.getLoggedInUser(), comment);
            return "redirect:/games/" + gameId + "/reviews/" + postId;
        } catch (Exception e) {
            e.printStackTrace();
            String message = URLEncoder.encode("Failed to post the comment. Please try again.",
                StandardCharsets.UTF_8);
            return "redirect:/games/" + gameId + "/reviews/" + postId + "?error=" + message;
        }
   }
}
