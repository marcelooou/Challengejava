package com.example.challenge.domain;

import java.util.List;

public class CreateOrderDTO {
  public Long userId;
  public List<OrderItemDTO> items;
  public static class OrderItemDTO {
    public Long productId;
    public Integer quantity;
  }
}
