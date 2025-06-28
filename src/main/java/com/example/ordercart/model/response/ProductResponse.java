package com.example.ordercart.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ProductResponse implements Serializable {
    private UUID userId;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
}
