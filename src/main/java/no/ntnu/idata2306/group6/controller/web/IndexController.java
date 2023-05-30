package no.ntnu.idata2306.group6.controller.web;

import jakarta.persistence.criteria.CriteriaBuilder;
import no.ntnu.idata2306.group6.entity.Info;
import no.ntnu.idata2306.group6.entity.Product;
import no.ntnu.idata2306.group6.service.AccessUserService;
import no.ntnu.idata2306.group6.service.InfoService;
import no.ntnu.idata2306.group6.service.ProductService;
import no.ntnu.idata2306.group6.service.UserService;
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


    private ProductService productService;

    private InfoService infoService;

    private AccessUserService userService;

    @Autowired
    public IndexController(ProductService productService, InfoService infoService, AccessUserService userService) {
        this.productService = productService;
        this.infoService = infoService;
        this.userService = userService;
    }

    /**
     * Get the index page of the main site.
     *
     * @return index
     */
    @GetMapping("/")
    public String home(Model model){

        List<Product> products = this.productService.getRandomProducts();
        List<Info> infos = new ArrayList<>();
        for (Product p : products) {
            infos.addAll((Collection<? extends Info>) infoService.findByProdId(p.getProductId()));
//                     Iterables.concat(infos, infoService.findByProdId(p.getProductId()));
        }

        model.addAttribute("featuredProduct", products);
        model.addAttribute("infos", infos);
        model.addAttribute("user", userService.getSessionUser());
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
