package com.example.ordercart.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.example.ordercart.common.constant.TblConstant.TBL_TXN_ORDER_CART;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = TBL_TXN_ORDER_CART)
@AllArgsConstructor
@Builder
public class OrderCart extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_cart_id") // FK in OrderItem table
    private List<OrderItem> items;
}

