package no.ntnu.idata2306.group6.controller.web;

import no.ntnu.idata2306.group6.service.AccessUserService;
import no.ntnu.idata2306.group6.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@CrossOrigin
public class LoginController {


    private AccessUserService userService;

    @Autowired
    public LoginController(AccessUserService userService) {
        this.userService = userService;
    }

    /**
     * Get the login page.
     *
     * @return login page
     */
    @GetMapping("/login")
    public String login() {
        return "log-in";
    }

    @GetMapping("/login")
    @RequestMapping(method = RequestMethod.POST)
    public String loginForm(Model model) {
        model.addAttribute("sessionUser", this.userService.getSessionUser());
        return "log-in";
    }
}
