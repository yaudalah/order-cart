package com.example.belajarspringboot.models.dto;

import com.example.belajarspringboot.models.OrderItem;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderCartDTO {
    private Long id;
    private String userId;
    private List<OrderItem> items;
}
