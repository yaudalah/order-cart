package com.example.ordercart.repository;

import com.example.ordercart.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM OrderItem oi WHERE oi.orderCartId = :cartId")
    int deleteAllByCartId(@Param("cartId") UUID cartId);

    List<OrderItem> findAllByCartId(UUID id);
}



