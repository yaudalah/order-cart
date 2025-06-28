package com.example.ordercart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

import static com.example.ordercart.common.constant.TblConstant.TBL_MST_PRODUCT;

@Entity
@Getter
@Setter
@Table(name = TBL_MST_PRODUCT)
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
    @Column(name = "user_id")
    private UUID userId;

    @Override
    protected void onCreate() {
        super.onCreate();
        if (this.getStock() <= 0) {
            this.setStock(0);
        }
    }
}

