package com.csci4370.finalproject.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.csci4370.finalproject.models.Review;
import com.csci4370.finalproject.models.User;
import com.csci4370.finalproject.services.ProfileService;
import com.csci4370.finalproject.services.UserService;

@Controller
@RequestMapping
public class ProfileController {
    
    private final UserService userService;
    private final ProfileService profileService;

    @Autowired
    public ProfileController(UserService userService, ProfileService profileService) {
        this.userService = userService;
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public ModelAndView webpage(@RequestParam(name = "error", required = false) String error) {
        ModelAndView mv = new ModelAndView("profile_page");
        
        User user = userService.getLoggedInUser();
        
        //If user somehow is not logged in
        if (user == null) {
            mv.setViewName("redirect:/login");
            return mv;
        }

        // Add User Info
        mv.addObject("firstName", user.getFirstName());
        mv.addObject("lastName", user.getLastName());
        mv.addObject("profileImagePath", user.getProfileImagePath()); 

        // Add Reviews
        List<Review> reviews = profileService.getUserReviews(user.getUserId());
        mv.addObject("reviews", reviews);

        return mv;
    }
}