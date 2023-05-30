package no.ntnu.idata2306.group6.controller.web;

import no.ntnu.idata2306.group6.service.AccessUserService;
import no.ntnu.idata2306.group6.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * A controller serving the html pages.
 */
@Controller
@CrossOrigin
public class HtmlPageController {
    @Autowired
    ProductService productService;

    @Autowired
    private AccessUserService userService;

    /**
     * Get the index page of the main site.
     *
     * @return index
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("user", userService.getSessionUser());
        return "index";
    }

    /**
     * Get the index page of the main site.
     *
     * @return index
     */
    @GetMapping("/home")
    public String home2(Model model) {
        model.addAttribute("user", userService.getSessionUser());
        return "index";
    }

    /**
     * Get the index page of the main site.
     *
     * @return index
     */
    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("user", userService.getSessionUser());
        return "index";
    }


    /**
     * Get the no-access page.
     *
     * @return no-access page
     */
    @GetMapping("/no-access")
    public String noAccess() {
        return "noAccess";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("user", userService.getSessionUser());
        return "admin";
    }

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


    @GetMapping("/payment")
    public String getPayment(Model model) {
        return "payment";
    }
}