package no.ntnu.idata2306.group6.controller.web;

import no.ntnu.idata2306.group6.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin
public class IndexController {


    private ProductService productService;

    @Autowired
    public IndexController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Get the index page of the main site.
     *
     * @return index
     */
    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("featuredProduct", this.productService.getRandomProducts());

        return "index";
    }

    /**
     * Get the index page of the main site.
     *
     * @return index
     */
    @GetMapping("/home")
    public String home2(Model model) {
        return home(model);
    }

    /**
     * Get the index page of the main site.
     *
     * @return index
     */
    @GetMapping("/index")
    public String index(Model model) {
        return home(model);
    }
}
