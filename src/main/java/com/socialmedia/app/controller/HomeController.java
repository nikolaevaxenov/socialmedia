package com.socialmedia.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public String home() {
        return "Hello, ";
    }

    @GetMapping("/user")
    public String user(Principal principal) {
        return "User page\nHello, " + principal.getName();
    }
}