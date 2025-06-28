package com.example.ordercart.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.example.ordercart.common.constant.TblConstant.TBL_TXN_INVOICE;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TBL_TXN_INVOICE)
public class Invoice extends BaseEntity {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "ref_id")
    private String refId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id") // FK in OrderItem table
    private List<OrderItem> items;

    @Column(name = "status")
    private String status;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 4)
    private BigDecimal totalPrice;
}

