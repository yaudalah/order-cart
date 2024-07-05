package com.example.belajarspringboot.models.DTO;

import com.example.belajarspringboot.models.OrderItem;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OrderCartDTO {
    private Long id;
    private UUID userId;
    private List<OrderItem> items;
}
