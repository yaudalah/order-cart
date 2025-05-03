package com.example.ordercart.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
public class ProductRequest {

    @NotNull(message = "Product name is required")
    private String name;

    private String description;

    @NotNull(message = "Product price is required")
    private BigDecimal price;

    @PositiveOrZero(message = "Stock cannot be negative")
    private int stock;
}
