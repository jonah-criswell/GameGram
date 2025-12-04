package com.csci4370.finalproject.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.csci4370.finalproject.services.GamesService;
import com.csci4370.finalproject.dto.GameWithPlatforms;
import com.csci4370.finalproject.models.Game;

@Controller
@RequestMapping("/games")
public class GameSearchController {

      private final GamesService gamesService;

      @Autowired
      public GameSearchController(GamesService gamesService) {
         this.gamesService = gamesService;
      }

      @GetMapping
      public ModelAndView webpage(@RequestParam(name = "query", required = false)  String query) {
         ModelAndView mv = new ModelAndView("games_page");
         String searchQuery = query;
         List<GameWithPlatforms> games = gamesService.searchGameByTitle(searchQuery);
         mv.addObject("games", games);
         return mv;
      }
}
