package no.ntnu.idata2306.group6.controller.web;

import io.swagger.v3.oas.annotations.Operation;
import no.ntnu.idata2306.group6.service.AccessUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin
public class LoginController {

    private final AccessUserService userService;

    @Autowired
    public LoginController(AccessUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    @Operation(
            summary = "Get the login page",
            description = "Returns the HTML page for the login"
    )

    public String login() {
        return "login";
    }

//    @GetMapping("/login")
//    @RequestMapping(method = RequestMethod.POST)
//    public String loginForm(Model model) {
//        model.addAttribute("sessionUser", this.userService.getSessionUser());
//        return "log-in";
//    }
}
