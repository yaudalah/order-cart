package com.example.ordercart.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class ProductResponse implements Serializable {
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
}
