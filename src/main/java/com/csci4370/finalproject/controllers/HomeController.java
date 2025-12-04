package com.csci4370.finalproject.controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.csci4370.finalproject.services.UserService;
import com.csci4370.finalproject.services.ReviewService;
import com.csci4370.finalproject.services.GamesService;
import com.csci4370.finalproject.services.PeopleService;
import com.csci4370.finalproject.models.User;
import com.csci4370.finalproject.models.FollowableUser;
import com.csci4370.finalproject.models.Game;
import com.csci4370.finalproject.services.FollowService;

@Controller
@RequestMapping
public class HomeController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final GamesService gamesService;
    private final PeopleService peopleService;
    private final FollowService followService;


    @Autowired
    public HomeController(UserService userService, ReviewService reviewService, GamesService gamesService, PeopleService peopleService, FollowService followService) {
        this.userService = userService;
        this.reviewService = reviewService;
        this.gamesService = gamesService;
        this.peopleService = peopleService;
        this.followService = followService;
    }

    @GetMapping
    public ModelAndView webpage(@RequestParam(name = "error", required = false) String error) {
        ModelAndView mv = new ModelAndView("home_page");

        if (!userService.isAuthenticated()) {
            mv.setViewName("redirect:/login");
            return mv;
        }

        User currentUser = userService.getLoggedInUser();

        try{
            List <FollowableUser> users = peopleService.getFollowableUsers(currentUser.getUserId());
            mv.addObject("users", users);

            if(users.isEmpty()){
                mv.addObject("isNoContent", true);
            }

        } catch (Exception e){
            mv.addObject("errorMessage", "Unable to load users.");
            e.printStackTrace();
        }



        // Redirect the user with an error message if there was an error.

        // try {
        //     List<Game> globalPopularGames = gamesService.getMostPopularGlobal();
        //     mv.addObject("posts", globalPopularGames);

        //     if (globalPopularGames.isEmpty()) {
        //         mv.addObject("isNoContent", true);
        //     }
        // } catch (Exception e) {
        //     mv.addObject("errorMessage", "Unable to load user posts.");
        //     e.printStackTrace();
        // }

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

    @PostMapping("/follow")
    public String followOrUnfollowUser(
            @RequestParam("followedId") String followedId,
            @RequestParam("isFollowed") boolean isFollow) {

        if (!userService.isAuthenticated()) {
            return "redirect:/login";
        }

        User currentUser = userService.getLoggedInUser();
        String currentUserId = currentUser.getUserId();

        try {
            if (isFollow) {
                // Currently following, so unfollow
                followService.unfollowUser(currentUserId, followedId);
            } else {
                // Currently not following, so follow
                followService.followUser(currentUserId, followedId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String message = URLEncoder.encode("Failed to (un)follow the user. Please try again.",
                    StandardCharsets.UTF_8);
            return "redirect:/?error=" + message;
        }

        return "redirect:/"; // Redirect back to your user list page
    }



}