package no.ntnu.idata2306.group6.controller.web;

import no.ntnu.idata2306.group6.entity.Category;
import no.ntnu.idata2306.group6.entity.Subscription;
import no.ntnu.idata2306.group6.entity.User;
import no.ntnu.idata2306.group6.service.AccessUserService;
import no.ntnu.idata2306.group6.service.CategoryService;
import no.ntnu.idata2306.group6.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

public class profilePageController {

    @Autowired
    private AccessUserService userService;

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/profile-page/{username}")
    public String getProfilePage(Model model, @PathVariable String username) {
        List<Subscription> subscriptions = this.subscriptionService.findByUser(this.userService.getSessionUser());
        model.addAttribute("subscriptions", subscriptions);

        return handleProfilePageRequest(username, model);
    }


    private String handleProfilePageRequest(String username, Model model) {
        User authenticatedUser = this.userService.getSessionUser();
        if (authenticatedUser == null) {
            return "no-access";
        }
        if (authenticatedUser.getEmail().equals(username)) {
            model.addAttribute("sessionUser", authenticatedUser);
        }
        return "profile-page";
    }

    @GetMapping("/admin/{username}")
    public String getAdmin(Model model, @PathVariable String username) {
        Iterable<Category> categories = this.categoryService.getAll();
        model.addAttribute("categories", categories);
        return handleAdminPageRequest(username, model);
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

    @GetMapping("/no-access")
    public String getNoAccess() {
        return "no-access";
    }
}
