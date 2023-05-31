package no.ntnu.idata2306.group6.controller.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.ntnu.idata2306.group6.entity.*;
import no.ntnu.idata2306.group6.entity.dto.UserDTO;
import no.ntnu.idata2306.group6.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@CrossOrigin
@Api(tags = "Profile Page Web API")
public class ProfilePageController {


    private final AccessUserService userService;

    private final SubscriptionService subscriptionService;

    private final CategoryService categoryService;

    private final UserService userService2;

    private final ProductService productService;

    private final InfoService infoService;
    @Autowired
    public ProfilePageController(AccessUserService userService,
                                 SubscriptionService subscriptionService,
                                 CategoryService categoryService,
                                 UserService userService2, ProductService productService,
                                 InfoService infoService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.categoryService = categoryService;
        this.userService2 = userService2;
        this.productService = productService;
        this.infoService = infoService;
    }

    @GetMapping("/profile-page")
    @ApiOperation("Get the profile page")
    public String getProfilePage(@ModelAttribute UserDTO profileData,
                                 Model model) {
        List<Subscription> subscriptions = this.subscriptionService.findByUser(this.userService.getSessionUser());
        model.addAttribute("subscriptions", subscriptions);

        List<Product> products = this.subscriptionService.findProductByUser(userService.getSessionUser());
        List<Info> infos = new ArrayList<>();
        for (Product p : products) {
            infos.addAll((Collection<? extends Info>) infoService.findByProdId(p.getProductId()));
        }

        model.addAttribute("user", userService.getSessionUser());
        model.addAttribute("subscribedProduct", products);
        model.addAttribute("infos", infos);

        return handleProfilePageRequest(profileData, model);
    }


    @GetMapping("/admin/{username}")
    @ApiOperation("Get the admin page")
    public String getAdmin(Model model, @PathVariable String username) {
        Iterable<Category> categories = this.categoryService.getAll();
        model.addAttribute("categories", categories);
        return handleAdminPageRequest(username, model);
    }

    private String handleProfilePageRequest(UserDTO postData, Model model) {
        User authenticatedUser = userService.getSessionUser();
        if (authenticatedUser != null && authenticatedUser.getEmail().equals(userService.getSessionUser().getEmail())) {
            model.addAttribute("user", authenticatedUser);
            if (postData != null) {
                boolean yes = true;
                if (yes) {//userService.updateProfile(authenticatedUser, postData)) {
                    model.addAttribute("successMessage", "Profile updated!");
                } else {
                    model.addAttribute("errorMessage", "Could not update profile data!");
                }
            }
            return "profilePage";
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
