package com.example.belajarspringboot.controllers;

import com.example.belajarspringboot.models.DTO.SuccessApiResponse;
import com.example.belajarspringboot.models.Product;
import com.example.belajarspringboot.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/")
    public Page<Product> getAllProducts(Pageable pageable) {
        return productService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public SuccessApiResponse<Object> createProduct(@RequestBody Product product) {
        return productService.create(product);
    }

    @PostMapping("/create-many")
    public List<Product> createManyProduct(@RequestBody List<Product> product) {
        return productService.bulkInsert(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productService.update(id, productDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        return productService.delete(id)
                .map(entity -> ResponseEntity.ok().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
