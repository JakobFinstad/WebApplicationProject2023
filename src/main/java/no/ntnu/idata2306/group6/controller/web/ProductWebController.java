package no.ntnu.idata2306.group6.controller.web;

import no.ntnu.idata2306.group6.entity.Info;
import no.ntnu.idata2306.group6.entity.Product;
import no.ntnu.idata2306.group6.service.AccessUserService;
import no.ntnu.idata2306.group6.service.InfoService;
import no.ntnu.idata2306.group6.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductWebController {
    @Autowired
    private ProductService productService;
    @Autowired
    private InfoService infoService;
    @Autowired
    private AccessUserService userService;

    @GetMapping
    public String getProducts(@RequestParam(required = false, name = "category") String categoryName, Model model) {

        Iterable<Product> products;
        List<Info> infos = new ArrayList<>();
        if (categoryName == null || categoryName.equals("all")) {
            products = this.productService.getAll();
            infos.addAll((Collection<? extends Info>) infoService.getAll());
        } else {
            products = this.productService.getAllByCategory(categoryName);
            for (Product p : products) {
                infos.addAll((Collection<? extends Info>) infoService.findByProdId(p.getProductId()));
//                     Iterables.concat(infos, infoService.findByProdId(p.getProductId()));
            }
        }

        model.addAttribute("products", products);
        model.addAttribute("infos", infos);
        model.addAttribute("user", userService.getSessionUser());
        return "products";
    }

    @GetMapping("/{id}")
    public String getOneProduct(Model model, @PathVariable("id") int id) {
        model.addAttribute("products", this.productService.findById(id));
        model.addAttribute("infos", infoService.findByProdId(id));
        return "singleProductPage";
    }

    @GetMapping("/{id}/payment")
    public String getPayment(Model model, @PathVariable("id") int id) {
        model.addAttribute("products", this.productService.findById(id));
        return "payment";
    }
}