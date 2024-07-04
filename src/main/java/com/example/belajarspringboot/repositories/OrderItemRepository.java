package com.example.belajarspringboot.repositories;

import com.example.belajarspringboot.models.OrderCart;
import com.example.belajarspringboot.models.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends BaseRepository<OrderItem, Long> {
}



