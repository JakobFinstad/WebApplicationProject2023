package no.ntnu.idata2306.group6.controller.web;

import no.ntnu.idata2306.group6.entity.Category;
import no.ntnu.idata2306.group6.entity.Subscription;
import no.ntnu.idata2306.group6.entity.User;
import no.ntnu.idata2306.group6.entity.dto.UserDTO;
import no.ntnu.idata2306.group6.service.AccessUserService;
import no.ntnu.idata2306.group6.service.CategoryService;
import no.ntnu.idata2306.group6.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@CrossOrigin
public class ProfilePageController {

    @Autowired
    private AccessUserService userService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/profile-page/{username}")
    public String getProfilePage(@ModelAttribute UserDTO profileData, Model model, @PathVariable String username) {
        List<Subscription> subscriptions = this.subscriptionService.findByUser(this.userService.getSessionUser());
        model.addAttribute("subscriptions", subscriptions);

        return handleProfilePageRequest(username, profileData, model);
    }


    @GetMapping("/admin/{username}")
    public String getAdmin(Model model, @PathVariable String username) {
        Iterable<Category> categories = this.categoryService.getAll();
        model.addAttribute("categories", categories);
        return handleAdminPageRequest(username, model);
    }

    private String handleProfilePageRequest(String username, UserDTO postData, Model model) {
        User authenticatedUser = userService.getSessionUser();
        if (authenticatedUser != null && authenticatedUser.getEmail().equals(username)) {
            model.addAttribute("user", authenticatedUser);
            if (postData != null) {
                if (userService.updateProfile(authenticatedUser, postData)) {
                    model.addAttribute("successMessage", "Profile updated!");
                } else {
                    model.addAttribute("errorMessage", "Could not update profile data!");
                }
            }
            return "user";
        } else {
            return "no-access";
        }
    }


    private String handleAdminPageRequest(String username, Model model) {
        User authenticatedUser = this.userService.getSessionUser();
        if (authenticatedUser == null) {
            return "no-access";
        }
        if (authenticatedUser.getEmail().equals(username) && authenticatedUser.hasRole("ADMIN")) {
            model.addAttribute("sessionUser", authenticatedUser);
        }
        return "admin";
    }
    
}
