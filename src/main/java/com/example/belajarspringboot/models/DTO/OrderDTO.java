package com.example.belajarspringboot.models.DTO;

import com.example.belajarspringboot.models.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private List<OrderItem> items;
    private String status;
    private BigDecimal total;
    private Long userId;
}

