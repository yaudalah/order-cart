package com.example.ordercart.entity;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import static com.example.ordercart.common.constant.TblConstant.TBL_TXN_ORDER_ITEM;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TBL_TXN_ORDER_ITEM)
public class OrderItem extends BaseEntity{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "order_cart_id")
    private UUID orderCartId;

    @NotNull
    private int quantity;
}

