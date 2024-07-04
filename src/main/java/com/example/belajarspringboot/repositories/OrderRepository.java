package com.example.belajarspringboot.repositories;

import com.example.belajarspringboot.models.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends BaseRepository<Order, Long> {
    List<Order> findByUserId(UUID userId);
}

