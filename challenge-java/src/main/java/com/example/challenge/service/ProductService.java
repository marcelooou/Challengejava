package com.example.challenge.service;

import com.example.challenge.domain.Product;
import com.example.challenge.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repo;
    
    public ProductService(ProductRepository repo) { 
        this.repo = repo; 
    }
    
    public List<Product> list() { 
        return repo.findAll(); 
    }
    
    public Product save(Product p) { 
        return repo.save(p); 
    }
    
    public void delete(Long id) { 
        repo.deleteById(id); 
    }
    
    public Product find(Long id) { 
        return repo.findById(id).orElse(null); 
    }
}