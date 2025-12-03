package com.csci4370.finalproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home_page"; // Will look for src/main/resources/templates/home.mustache
    }
}