package no.ntnu.idata2306.group6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * Get the user page for the user.
     *
     * @return user page
     */
    @GetMapping("user")
    @PreAuthorize("hasRole('USER')")
    public String userPage() {
        return "This is the users page";
    }

    @GetMapping("admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPage() {
        return "adminPage";
    }
}