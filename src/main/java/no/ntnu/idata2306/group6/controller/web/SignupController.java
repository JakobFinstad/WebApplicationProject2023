package no.ntnu.idata2306.group6.controller.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.ntnu.idata2306.group6.entity.dto.SignupDTO;
import no.ntnu.idata2306.group6.service.AccessUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@CrossOrigin
@Api(tags = "Signup Web API")
public class SignupController {

    private AccessUserService accessUserService;

    @Autowired
    public SignupController(AccessUserService accessUserService) {
        this.accessUserService = accessUserService;
    }

    /**
     * Get the signup page.
     *
     * @return signup page
     */
    @GetMapping("/signup")
    @ApiOperation("Get the signup page")
    public String signup() {
        return "sign-up";
    }

    @PostMapping("/signup")
    @ApiOperation("Create a new user")
    public String createUser(Model model, @ModelAttribute SignupDTO signupDto) {
        String errormessage = this.accessUserService.tryCreateNewUser(signupDto.getFirstName(),
                signupDto.getLastName(), signupDto.getEmail(), signupDto.getPhoneNumber(), signupDto.getPassword(), signupDto.getAge());
        if (errormessage != null) {
            model.addAttribute("user", signupDto);
            model.addAttribute("errorMessage", errormessage);
            return "sign-up";
        }
        return "profilePage";
    }

}
