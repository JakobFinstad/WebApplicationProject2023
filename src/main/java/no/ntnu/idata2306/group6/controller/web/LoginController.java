package no.ntnu.idata2306.group6.controller.web;

import no.ntnu.idata2306.group6.service.AccessUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class LoginController {

    @Autowired
    AccessUserService userService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("sessionUser", this.userService.getSessionUser());
        return "login";
    }
}
