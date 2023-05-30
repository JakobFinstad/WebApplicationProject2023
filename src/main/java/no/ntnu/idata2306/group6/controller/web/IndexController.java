package no.ntnu.idata2306.group6.controller.web;

import io.swagger.v3.oas.annotations.Operation;
import no.ntnu.idata2306.group6.entity.Info;
import no.ntnu.idata2306.group6.entity.Product;
import no.ntnu.idata2306.group6.service.AccessUserService;
import no.ntnu.idata2306.group6.service.InfoService;
import no.ntnu.idata2306.group6.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Controller
@CrossOrigin
public class IndexController {

    private final ProductService productService;
    private final InfoService infoService;
    private final AccessUserService userService;

    @Autowired
    public IndexController(ProductService productService, InfoService infoService, AccessUserService userService) {
        this.productService = productService;
        this.infoService = infoService;
        this.userService = userService;
    }

    @GetMapping("/")
    @Operation(
            summary = "Get the index page of the main site",
            description = "Returns the HTML page for the main site's index"
    )
    public String home(Model model) {
        List<Product> products = productService.getRandomProducts();
        List<Info> infos = new ArrayList<>();
        for (Product p : products) {
            infos.addAll((Collection<? extends Info>) infoService.findByProdId(p.getProductId()));
        }
        model.addAttribute("featuredProduct", products);
        model.addAttribute("infos", infos);
        model.addAttribute("user", userService.getSessionUser());
        return "index";
    }

    @GetMapping("/home")
    @Operation(
            summary = "Get the index page of the main site",
            description = "Returns the HTML page for the main site's index"
    )
    public String home2(Model model) {
        return home(model);
    }

    @GetMapping("/index")
    @Operation(
            summary = "Get the index page of the main site",
            description = "Returns the HTML page for the main site's index"
    )
    public String index(Model model) {
        return home(model);
    }
}
