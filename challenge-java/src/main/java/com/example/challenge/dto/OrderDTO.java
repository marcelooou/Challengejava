package com.example.challenge.domain;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
  public Long id;
  public Long userId;
  public LocalDateTime createdAt;
  public String status;
  public List<CreateOrderDTO.OrderItemDTO> items;
}
