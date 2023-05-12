package no.ntnu.idata2306.group6.controller.web;

import no.ntnu.idata2306.group6.entity.Category;
import no.ntnu.idata2306.group6.entity.Product;
import no.ntnu.idata2306.group6.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("products", products);
        model.addAttribute("seperator", ",");
        return "products";
    }

    @GetMapping("products/{id}")
    public String getOneProduct(Model model, @ModelAttribute("id")@PathVariable int id) {
        model.addAttribute("products", this.productService.findById(id));
        return "singleProductPage";
    }
}