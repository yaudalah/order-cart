package com.example.ordercart.controller;

import com.example.ordercart.model.request.ProductRequest;
import com.example.ordercart.model.response.ApiResponse;
import com.example.ordercart.model.response.ProductResponse;
import com.example.ordercart.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse> fetchAllProduct(Pageable pageable) {
        return ApiResponse.ok(productService.getAllProducts(pageable));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createProduct(@RequestBody ProductRequest productRequest) {
        log.info("[ProductController] product payload: {}", productRequest);
        ProductResponse productResponse = productService.createProduct(productRequest);
        return ApiResponse.ok(productResponse);
    }
}
