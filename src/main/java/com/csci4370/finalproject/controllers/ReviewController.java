package com.csci4370.finalproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.csci4370.finalproject.services.ReviewService;

@Controller
@RequestMapping("/games/{gameId}/makereview")
public class ReviewController {
   private final ReviewService reviewService;

   @Autowired
   public ReviewController(ReviewService reviewService) {
      this.reviewService = reviewService;
   }

   @GetMapping
   public ModelAndView webpage(@PathVariable String gameId) {
      ModelAndView mv = new ModelAndView("make_review_page");
      mv.addObject("reviews", reviewService.makeReview(0, Integer.parseInt(gameId), "", 0, null));
      return mv;
   }
}
