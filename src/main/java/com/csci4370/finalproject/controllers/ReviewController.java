package com.csci4370.finalproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.csci4370.finalproject.services.ReviewService;
import com.csci4370.finalproject.services.UserService;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/games/{gameId}/makereview")
public class ReviewController {
   private final ReviewService reviewService;
   private final UserService userService;

   @Autowired
   public ReviewController(ReviewService reviewService, UserService userService) {
      this.reviewService = reviewService;
      this.userService = userService;
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
}
