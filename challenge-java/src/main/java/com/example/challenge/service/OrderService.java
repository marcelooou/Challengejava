package com.example.challenge.service;

import com.example.challenge.domain.*;
import com.example.challenge.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository repo;
    private final ProductRepository productRepo;
    
    public OrderService(OrderRepository repo, ProductRepository productRepo) {
        this.repo = repo; 
        this.productRepo = productRepo;
    }

    public Order create(Order o) { 
        return repo.save(o); 
    }
    public List<Order> list() { 
        return repo.findAll(); 
    }
    public Order find(Long id) { 
        return repo.findById(id).orElse(null); 
    }
    public void delete(Long id) { 
        repo.deleteById(id); 
    }
    public Order approve(Long id) {
        // CORREÇÃO: 'var o' foi trocado por 'final Order o'
        final Order o = find(id);
        if (o == null) return null;
        o.setStatus("APPROVED");
        return repo.save(o);
    }
}