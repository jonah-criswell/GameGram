package com.csci4370.finalproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.csci4370.finalproject.services.UserService;

@Controller
@RequestMapping
public class HomeController {

    private final UserService userService;
    private final GamesService gamesService;
    @Autowired
    public HomeController(UserService userService, GamesService gamesService) {
        this.userService = userService;
        this.gamesService = gamesService;
    }

    @GetMapping
    public ModelAndView webpage(@RequestParam(name = "error", required = false) String error) {
        // See notes on ModelAndView in BookmarksController.java.
        ModelAndView mv = new ModelAndView("home_page");

        if (!userService.isAuthenticated()) {
            mv.setViewName("redirect:/login");
            return mv;
        }

        User currentUser = userService.getLoggedInUser();

        try {
            List<Game> globalPopularGames = gamesService.getMostPopularGlobal();
            mv.addObject("posts", globalPopularGames);

            if (globalPopularGames.isEmpty()) {
                mv.addObject("isNoContent", true);
            }
        } catch (Exception e) {
            mv.addObject("errorMessage", "Unable to load user posts.");
            e.printStackTrace();
        }

        return mv;
    }

}