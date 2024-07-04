package com.example.belajarspringboot.services;

import com.example.belajarspringboot.models.Product;
import com.example.belajarspringboot.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends BaseService<Product, Long> {

    @Autowired
    public ProductService(ProductRepository productRepository) {
        super(productRepository);
    }

    @Override
    protected void updateEntity(Product existingEntity, Product entityDetails) {
        existingEntity.setName(entityDetails.getName());
        existingEntity.setDescription(entityDetails.getDescription());
        existingEntity.setPrice(entityDetails.getPrice());
    }
}

