package no.ntnu.idata2306.group6.controller.web;

import io.swagger.v3.oas.annotations.Operation;
import no.ntnu.idata2306.group6.service.AccessUserService;
import no.ntnu.idata2306.group6.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin
public class HtmlPageController {

    private final ProductService productService;
    private final AccessUserService userService;

    @Autowired
    public HtmlPageController(ProductService productService, AccessUserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/no-access")
    @Operation(
            summary = "Get the no-access page",
            description = "Returns the HTML page for no access"
    )
    public String noAccess() {
        return "noAccess";
    }

    @GetMapping("/admin")
    @Operation(
            summary = "Get the admin page",
            description = "Returns the HTML page for the admin"
    )
    public String adminPage(Model model) {
        model.addAttribute("user", userService.getSessionUser());
        return "admin";
    }
}
