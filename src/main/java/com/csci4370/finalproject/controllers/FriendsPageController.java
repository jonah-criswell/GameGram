package com.csci4370.finalproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.csci4370.finalproject.models.User;
import com.csci4370.finalproject.services.PeopleService;
// import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.csci4370.finalproject.services.UserService;


@Controller
@RequestMapping
public class FriendsPageController {

    private final PeopleService peopleService;
    private final UserService userService;

    public FriendsPageController(PeopleService peopleService, UserService userService) {
        this.peopleService = peopleService;
        this.userService = userService;
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
                mv.addObject("friends", userService.getUserFollowedFriends(currentUser.getUserId()));
                //mv.addObject("friendRequests", peopleService.getFriendRequests(currentUser.getUserId()));
            } catch (Exception e) {
                mv.addObject("errorMessage", "Unable to load friends data.");
                e.printStackTrace();
            }


        return mv;
    }

    
}
