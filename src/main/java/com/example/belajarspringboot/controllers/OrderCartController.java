package com.example.belajarspringboot.controllers;

import com.example.belajarspringboot.models.DTO.SuccessApiResponse;
import com.example.belajarspringboot.models.OrderCart;
import com.example.belajarspringboot.models.OrderItem;
import com.example.belajarspringboot.services.OrderCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@CrossOrigin
public class OrderCartController {

    private final OrderCartService orderCartService;

    @PostMapping("/add")
    public SuccessApiResponse<Object> addProductToCart(@RequestBody OrderItem orderItem) {
        return orderCartService.addProductToCart(orderItem);
    }

    @GetMapping("/")
    public ResponseEntity<OrderCart> getCart() {
        return ResponseEntity.ok(orderCartService.getById(1L).orElse(new OrderCart()));
    }
}

