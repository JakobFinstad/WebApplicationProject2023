package no.ntnu.idata2306.group6.controller.web;

import io.swagger.annotations.ApiOperation;
import no.ntnu.idata2306.group6.entity.Info;
import no.ntnu.idata2306.group6.entity.Product;
import no.ntnu.idata2306.group6.entity.User;
import no.ntnu.idata2306.group6.service.AccessUserService;
import no.ntnu.idata2306.group6.service.InfoService;
import no.ntnu.idata2306.group6.service.ProductService;
import no.ntnu.idata2306.group6.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductWebController {

    private final ProductService productService;
    private final InfoService infoService;

    private final AccessUserService userService;
    private final SubscriptionService subscriptionService;

    @Autowired
    public ProductWebController(ProductService productService,
                                InfoService infoService,
                                AccessUserService userService,
                                SubscriptionService subscriptionService) {
        this.productService = productService;
        this.infoService = infoService;
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

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
        model.addAttribute("users", userService.getSessionUser());
        return "payment";
    }

    @Transactional
    @PostMapping("/{id}/subscription/delete")
    @ApiOperation("Unsubscribe from  a product")
    public String unsubscribeFromProduct(Model model, @PathVariable("id") int productId) {
        subscriptionService.removeByProductIdAndUserId(productId, userService.getSessionUser().getUserId());
        return "profilePage";
    }
}
