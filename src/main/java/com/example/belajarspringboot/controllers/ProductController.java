package com.example.belajarspringboot.controllers;

import com.example.belajarspringboot.models.dto.SuccessApiResponse;
import com.example.belajarspringboot.models.Product;
import com.example.belajarspringboot.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/")
    public SuccessApiResponse<Object> getAllProducts(Pageable pageable) {
        return productService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("create")
    public SuccessApiResponse<Object> createProduct(@RequestBody Product product) {
        return productService.create(product);
    }

    @PostMapping("create-many")
    public List<Product> createManyProduct(@RequestBody List<Product> product) {
        return productService.bulkInsert(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productService.update(id, productDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.delete(id);
    }
}
