package com.example.challenge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller responsável por endpoints relacionados a Produtos.
 * Foi movido para este arquivo para resolver o erro de classe pública.
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    @GetMapping
    public String listProducts() {
        // Retorna o nome da sua view de produtos (ex: products.html)
        return "products"; 
    }
}
