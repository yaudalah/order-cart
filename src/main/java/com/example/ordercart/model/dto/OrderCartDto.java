package com.example.ordercart.model.dto;

import com.example.ordercart.entity.OrderItem;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderCartDto {
    private Long id;
    private String userId;
    private List<OrderItem> items;
}
