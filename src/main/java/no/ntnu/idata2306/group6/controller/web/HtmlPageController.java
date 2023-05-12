package no.ntnu.idata2306.group6.controller.web;

import no.ntnu.idata2306.group6.entity.Product;
import no.ntnu.idata2306.group6.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui. Model;
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
     * Get the index page of the main site.
     *
     * @return index
     */
    @GetMapping("/home")
    public String home2() {
        return home();
    }

    /**
     * Get the index page of the main site.
     *
     * @return index
     */
    @GetMapping("/index")
    public String index() {
        return home();
    }

    /**
     * Get the product page.
     *
     * @return product page
     */
  /*  @GetMapping("/products")
    public String products(Model model){
        Iterable<Product> products = productService.getAll();
        model.addAttribute("products", products);
        return "products";
    }*/

    /**
     * Get the login page.
     *
     * @return login page
     */
    @GetMapping("/login")
    public String login() {
        return "log-in";
    }

    /**
     * Get the signup page.
     *
     * @return signup page
     */
    @GetMapping("/signup")
    public String signup() {return "sign-up";}

    /**
     * Get the profile page.
     *
     * @return profile page
     */
     @GetMapping("/profile-page")
     public String profilePage() {return "profilePage";}

    /**
     * Get the accounting solution page.
     *
     * @return accounting solution page
     */
    @GetMapping("/products/1")
    public String accountingSolution() {
        return "singleProductPage";
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

    /**
     * Get the admin page.
     *
     * @return admin page
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPage() {
        return "adminPage";
    }
}