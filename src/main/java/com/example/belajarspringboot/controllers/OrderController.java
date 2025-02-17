package com.example.belajarspringboot.controllers;

import com.example.belajarspringboot.models.dto.SuccessApiResponse;
import com.example.belajarspringboot.models.dto.SuccessListApiResponse;
import com.example.belajarspringboot.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/orders")
@CrossOrigin
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/")
    public SuccessApiResponse<Object> getAllOrders(Pageable pageable) {
        return orderService.getAll(pageable);
    }

    @GetMapping("user/{userId}")
    public SuccessListApiResponse<Object> getOrdersByUserId(@PathVariable String userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @PostMapping("checkout")
    public SuccessApiResponse<Object> checkout() {
        return orderService.placeOrder();
    }
}

