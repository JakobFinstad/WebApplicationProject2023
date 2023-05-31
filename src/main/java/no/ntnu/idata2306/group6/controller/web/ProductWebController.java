package no.ntnu.idata2306.group6.controller.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.ntnu.idata2306.group6.entity.Info;
import no.ntnu.idata2306.group6.entity.Product;
import no.ntnu.idata2306.group6.entity.Subscription;
import no.ntnu.idata2306.group6.entity.User;
import no.ntnu.idata2306.group6.service.AccessUserService;
import no.ntnu.idata2306.group6.service.InfoService;
import no.ntnu.idata2306.group6.service.ProductService;
import no.ntnu.idata2306.group6.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    @ApiOperation("Get all products")
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
    @ApiOperation("Get a product by ID")
    public String getOneProduct(Model model, @PathVariable("id") int id) {
        boolean owned = false;

        for (User user: subscriptionService.findUserByProductId(id)
             ) {
            if (userService.getSessionUser() == user) {
                owned = true;
            }
        }

        model.addAttribute("owned", owned);
        model.addAttribute("products", this.productService.findById(id));
        model.addAttribute("infos", infoService.findByProdId(id));

        return "singleProductPage";
    }

    @GetMapping("/{id}/payment")
    @ApiOperation("Get payment information for a product")
    public String getPayment(Model model, @PathVariable("id") String id,
                             @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        int prodId = Integer.parseInt(id);
        model.addAttribute("products", productService.findById(Integer.parseInt(id)));
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "payment";
    }
}
