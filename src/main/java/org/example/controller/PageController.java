package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String getHomePage() {
        return "home-page";
    }

    @GetMapping("/login")
    public String getLogInPage() {
        return "auth/login";
    }
}
