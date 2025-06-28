package com.example.ordercart.controller;

import com.example.ordercart.entity.OrderItem;
import com.example.ordercart.model.response.ApiResponse;
import com.example.ordercart.security.annotation.CheckUserPermission;
import com.example.ordercart.service.OrderCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.ordercart.common.constant.UrlPathsConstant.ORDER_CART;
import static com.example.ordercart.common.constant.UrlPathsConstant.URL_PREFIX_V1;

@Slf4j
@RestController
@RequestMapping(URL_PREFIX_V1 + ORDER_CART)
@RequiredArgsConstructor
public class OrderCartController {
    private final OrderCartService orderCartService;

    @PostMapping
    @CheckUserPermission
    public ResponseEntity<ApiResponse> addProductsToCart(@RequestBody OrderItem orderItem) {
        log.info("[OrderCartController] Creating order cart: {}", orderItem);
        return ApiResponse.ok(orderCartService.addProductsToCart(orderItem));
    }
}
