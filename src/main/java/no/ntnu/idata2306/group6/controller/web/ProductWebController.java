package no.ntnu.idata2306.group6.controller.web;

import no.ntnu.idata2306.group6.entity.Category;
import no.ntnu.idata2306.group6.entity.Info;
import no.ntnu.idata2306.group6.entity.Product;
import no.ntnu.idata2306.group6.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductWebController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public String getProducts(@RequestParam(required = false) Category category, Model model) {

        Iterable<Product> products;
        if (category == null) {
            products = this.productService.getAll();
        } else {
            products = this.productService.getAllByCategory(category.getCategoryName());
        }

        List<Info> infos = new ArrayList<>();
        for (Product p : products) {
            infos.addAll((Collection<? extends Info>) productService.getInfo(p.getProductId()));
        }
        model.addAttribute("infos", infos.iterator());
        model.addAttribute("products", products);
        model.addAttribute("separator", ",");
        return "products";
    }

    @GetMapping("products/{id}")
    public String getOneProduct(Model model, @ModelAttribute("id")@PathVariable int id) {
        model.addAttribute("product", this.productService.findById(id));
        model.addAttribute("info", this.productService.getInfo(id));
        return "singleProductPage";
    }
}