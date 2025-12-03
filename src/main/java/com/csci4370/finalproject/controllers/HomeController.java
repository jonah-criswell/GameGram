package com.csci4370.finalproject.controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.csci4370.finalproject.services.UserService;
import com.csci4370.finalproject.services.ReviewService;

@Controller
@RequestMapping
public class HomeController {

    private final UserService userService;
    private final ReviewService reviewService;

    @Autowired
    public HomeController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public ModelAndView webpage(@RequestParam(name = "error", required = false) String error) {
        // See notes on ModelAndView in BookmarksController.java.
        ModelAndView mv = new ModelAndView("home_page");

        if (!userService.isAuthenticated()) {
            mv.setViewName("redirect:/login");
            return mv;
        }
        return mv;
    }

    /**
     * This function handles the /createreview URL.
     * This handles a post request that is going to be a form submission.
     * The form for this can be found in the home page. The form has a
     * input field with name = posttext. Note that the @RequestParam
     * annotation has the same name. This makes it possible to access the value
     * from the input from the form after it is submitted.
     */
    @PostMapping("/createreview")
    public String createReview(@RequestParam(name = "posttext") int hoursPlayed ,String postText, int reviewRating) {
        System.out.println("User is creating a: " + postText);

        // Redirect the user if the post creation is a success.
        try {

            reviewService.makeReview(hoursPlayed, postText, reviewRating, userService.getLoggedInUser());
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            String message = URLEncoder.encode("Failed to create the post. Please try again.",
                StandardCharsets.UTF_8);
        return "redirect:/?error=" + message;
        // return "redirect:/";

        }
    }   

}