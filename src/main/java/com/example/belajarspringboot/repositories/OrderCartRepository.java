package com.example.belajarspringboot.repositories;

import com.example.belajarspringboot.models.OrderCart;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderCartRepository extends BaseRepository<OrderCart, Long> {
    Optional<OrderCart> findByUserUserId(UUID userId);
}
