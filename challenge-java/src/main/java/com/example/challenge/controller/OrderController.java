package com.example.challenge.controller;

import com.example.challenge.service.*;
import com.example.challenge.domain.*;
import com.example.challenge.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List; // Importação adicionada para a List
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;
    private final ModelMapper mapper;

    public OrderController(OrderService orderService, ProductService productService, UserService userService, ModelMapper mapper) {
        this.orderService = orderService; 
        this.productService = productService; 
        this.userService = userService; 
        this.mapper = mapper;
    }

    // flow: create order page (choose products)
    @GetMapping("/create")
    public String createForm(Model m) {
        m.addAttribute("products", productService.list());
        return "orders/create";
    }

    @PostMapping("/create")
    public String create(@RequestParam Long userId, @RequestParam Long[] productId, @RequestParam Integer[] qty) {
        Order o = new Order();
        o.setUserId(userId);
        for (int i = 0; i < productId.length; i++) {
            Product p = productService.find(productId[i]);
            OrderItem it = new OrderItem();
            it.setProductId(p.getId());
            it.setQuantity(qty[i]);
            it.setUnitPrice(p.getPrice());
            o.getItems().add(it);
        }
        orderService.create(o);
        return "redirect:/orders/mine?userId="+userId;
    }

    @GetMapping("/mine")
    public String mine(@RequestParam Long userId, Model m) {
        // CORREÇÃO: 'var list' foi trocado por 'final List<Order> list'
        final List<Order> list = orderService.list().stream().filter(o -> o.getUserId().equals(userId)).collect(Collectors.toList());
        m.addAttribute("orders", list);
        return "orders/mine";
    }

    @GetMapping("/admin")
    public String adminList(Model m) {
        m.addAttribute("orders", orderService.list());
        return "orders/admin_list";
    }

    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id) {
        orderService.approve(id);
        return "redirect:/orders/admin";
    }
}