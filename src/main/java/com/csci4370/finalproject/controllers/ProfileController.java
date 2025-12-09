package com.csci4370.finalproject.controllers;

import java.util.List;
import java.sql.SQLException;

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
public ModelAndView webpage(
        @RequestParam(name = "error", required = false) String error,
        @RequestParam(name = "userId", required = false) String userId) {
    ModelAndView mv = new ModelAndView("profile_page");
    
    User loggedInUser = userService.getLoggedInUser();
    
    // If user somehow is not logged in
    if (loggedInUser == null) {
        mv.setViewName("redirect:/login");
        return mv;
    }

    // Determine which user's profile to show
    User profileUser;
    if (userId != null && !userId.isEmpty()) {
        // Show another user's profile
        try {
            profileUser = userService.getUserById(userId);
            if (profileUser == null) {
                mv.addObject("errorMessage", "User not found");
                profileUser = loggedInUser; // Fallback to logged in user
            }
        } catch (SQLException e) {
            mv.addObject("errorMessage", "Unable to load user profile");
            profileUser = loggedInUser; // Fallback to logged in user
            e.printStackTrace();
        }
    } else {
        // Show logged in user's own profile
        profileUser = loggedInUser;
    }

    // Add User Info
    mv.addObject("firstName", profileUser.getFirstName());
    mv.addObject("lastName", profileUser.getLastName());
    mv.addObject("profileImagePath", profileUser.getProfileImagePath()); 
    mv.addObject("isOwnProfile", profileUser.getUserId().equals(loggedInUser.getUserId()));

    // Add Reviews
    try {
        List<Review> reviews = profileService.getUserReviews(profileUser.getUserId());
        mv.addObject("reviews", reviews);
    } catch (Exception e) {
        mv.addObject("errorMessage", "Unable to load reviews");
        e.printStackTrace();
    }

    return mv;
}
}