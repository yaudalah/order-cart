package com.example.belajarspringboot.controllers;

import com.example.belajarspringboot.models.DTO.SuccessApiResponse;
import com.example.belajarspringboot.models.DTO.SuccessListApiResponse;
import com.example.belajarspringboot.models.Order;
import com.example.belajarspringboot.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/")
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderService.getAll(pageable);
    }

    @GetMapping("/user/{userId}")
    public SuccessListApiResponse<Object> getOrdersByUserId(@PathVariable UUID userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @PostMapping("/checkout")
    public SuccessApiResponse<Object> checkout() {
        return orderService.placeOrder();
    }
}

