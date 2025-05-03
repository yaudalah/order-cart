package com.example.ordercart.repository;

import com.example.ordercart.entity.OrderCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderCartRepository extends JpaRepository<OrderCart, UUID> {
    Optional<OrderCart> findOrderCartByUserId(@Param("userId") UUID userId);
}
