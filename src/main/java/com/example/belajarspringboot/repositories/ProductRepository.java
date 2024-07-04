package com.example.belajarspringboot.repositories;

import com.example.belajarspringboot.models.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends BaseRepository<Product, Long> {
}

