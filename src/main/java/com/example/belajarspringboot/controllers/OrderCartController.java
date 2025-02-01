package com.example.belajarspringboot.controllers;

import com.example.belajarspringboot.models.dto.SuccessApiResponse;
import com.example.belajarspringboot.models.dto.SuccessListApiResponse;
import com.example.belajarspringboot.models.OrderItem;
import com.example.belajarspringboot.services.OrderCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@CrossOrigin
public class OrderCartController {

    private final OrderCartService orderCartService;

    @PostMapping("add")
    public SuccessApiResponse<Object> addProductToCart(@RequestBody @Valid OrderItem orderItem) {
        return orderCartService.addProductToCart(orderItem);
    }

    @GetMapping("/{userId}")
    public SuccessListApiResponse<Object> getCartByUserId(@PathVariable @Valid String userId) {
        return orderCartService.getCartByUserId(userId);
    }
}

