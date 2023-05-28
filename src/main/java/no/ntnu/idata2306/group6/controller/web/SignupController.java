package no.ntnu.idata2306.group6.controller.web;

import no.ntnu.idata2306.group6.entity.dto.SignupDTO;
import no.ntnu.idata2306.group6.security.AccessUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

public class SignupController {

    private AccessUserService accessUserService;

    /**
     * Get the signup page.
     *
     * @return signup page
     */
    @GetMapping("/signup")
    public String signup() {return "sign-up";}

    @PostMapping("/signup")
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

    @GetMapping("/profile-page")
    public String getProfilePage() {
        return "profilePage";
    }
}
