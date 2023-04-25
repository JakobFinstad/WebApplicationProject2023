package no.ntnu.idata2306.group6.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * A controller serving the html pages.
 */
@Controller
@CrossOrigin
public class HtmlPageController {

    /**
     * Get the index page of the main site.
     *
     * @return index
     */
    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/products")
    public String products(){
        return "products";
    }

    @GetMapping("/login")
    public String login() {
        return "log-in";
    }
    
    @GetMapping("/signup")
    public String signup() {return "sign-up";}

    @GetMapping("/accountingSolution")
    public String accountingSolution() {return "proFlexAccountingSolution";}


    /**
     * Get the user page for the user.
     *
     * @return user page
     */
    @GetMapping("/this_user")
    @PreAuthorize("hasRole('USER')")
    public String userPage() {
        return "This is the users page";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPage() {
        return "adminPage";
    }
}