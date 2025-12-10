package com.csci4370.finalproject.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.csci4370.finalproject.models.FollowableUser;
import com.csci4370.finalproject.models.Review;
import com.csci4370.finalproject.models.User;
import com.csci4370.finalproject.services.PeopleService;
import com.csci4370.finalproject.services.ReviewService;
import com.csci4370.finalproject.services.UserService;


@Controller
@RequestMapping
public class FriendsPageController {

    private final PeopleService peopleService;
    private final UserService userService;
    private final ReviewService reviewService;

    public FriendsPageController(PeopleService peopleService, UserService userService, ReviewService reviewService) {
        this.peopleService = peopleService;
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @GetMapping("/friends")
    public ModelAndView webpage() {
        ModelAndView mv = new ModelAndView("friends_page");

        if (!userService.isAuthenticated()) {
            mv.setViewName("redirect:/login");
            return mv;
        }

        User currentUser = userService.getLoggedInUser();

            try {
                // Retrieve the list of friends (followed users) for the current user.
                List<FollowableUser> friendsList = peopleService.getFollowedUsers(currentUser.getUserId());
                for (FollowableUser user1 : friendsList) {
                    Review recentGame = reviewService.getRecentReviewsFromFollowedUsers(user1.getUserId());
                    user1.setRecentGame(recentGame);
                }
                mv.addObject("friends", friendsList);
            } catch (Exception e) {
                mv.addObject("errorMessage", "Unable to load friends data.");
                e.printStackTrace();
            }


        return mv;
    }

    
}
