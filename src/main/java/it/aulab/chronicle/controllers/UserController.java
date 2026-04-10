package it.aulab.chronicle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// import it.aulab.chronicle.services.UserService;

@Controller
public class UserController {
    
    @Autowired
    // private UserService userService;

    /* Rotta Home */
    @GetMapping("/")
    public String home() {
        return "home";
    }
    
}
