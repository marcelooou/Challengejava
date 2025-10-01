package com.example.challenge.domain;

import jakarta.persistence.*;

@Entity
@Table(name="order_items")
public class OrderItem {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private Integer quantity;
    private Double unitPrice;

    // getters/setters
    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }
    public Long getProductId() { 
        return productId; 
    }
    public void setProductId(Long productId) { 
        this.productId = productId; 
    }
    public Integer getQuantity() { 
        return quantity; 
    }
    public void setQuantity(Integer quantity) { 
        this.quantity = quantity; 
    }
    public Double getUnitPrice() { 
        return unitPrice; 
    }
    public void setUnitPrice(Double unitPrice) { 
        this.unitPrice = unitPrice; 
    }
}