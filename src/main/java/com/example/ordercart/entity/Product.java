package com.example.ordercart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "tbl_mst_product")
@ToString
public class Product extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "price", nullable = false, precision = 10, scale = 4)
    private BigDecimal price;
    @Column(name = "stock")
    private int stock;

    @Override
    protected void onCreate() {
        super.onCreate();
        if (this.getStock() <= 0) {
            this.setStock(0);
        }
    }
}

