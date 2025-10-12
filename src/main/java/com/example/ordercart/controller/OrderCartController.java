package com.example.ordercart.controller;

import com.example.ordercart.entity.OrderItem;
import com.example.ordercart.model.response.ApiResponse;
import com.example.ordercart.security.annotation.CheckUserPermission;
import com.example.ordercart.service.OrderCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

import static com.example.ordercart.common.constant.UrlPathsConstant.ORDER_CART;
import static com.example.ordercart.common.constant.UrlPathsConstant.URL_PREFIX_V1;

@Slf4j
@RestController
@RequestMapping(URL_PREFIX_V1 + ORDER_CART)
@RequiredArgsConstructor
public class OrderCartController {
    private final OrderCartService orderCartService;

    @CheckUserPermission
    @PostMapping
    public ResponseEntity<ApiResponse> addProductsToCart(@RequestBody @Valid OrderItem orderItem) {
        log.info("[OrderCartController] Creating order cart: {}", orderItem);
        return ApiResponse.ok(orderCartService.addProductsToCart(orderItem));
    }

    @CheckUserPermission
    @GetMapping
    public ResponseEntity<ApiResponse> getOrderCart(Pageable pageable) {
        log.info("[OrderCartController] Fetching order cart");
        return ApiResponse.ok(orderCartService.getOrderCart(pageable));
    }

    @CheckUserPermission
    @PostMapping("remove")
    public ResponseEntity<ApiResponse> removeProductFromCart(List<UUID> productId) {
        log.info("[OrderCartController] Removing product from cart: {}", productId);
        return ApiResponse.ok(orderCartService.removeProductFromCart(productId));
    }

    @CheckUserPermission
    @GetMapping("clear")
    public ResponseEntity<ApiResponse> clearOrderCart() {
        log.info("[OrderCartController] Clearing order cart");
        return ApiResponse.ok(orderCartService.clearOrderCart());
    }

    @CheckUserPermission
    @PostMapping("checkout")
    public ResponseEntity<ApiResponse> checkoutOrderCart() {
        log.info("[OrderCartController] Checking out order cart");
        return ApiResponse.ok(orderCartService.checkoutOrderCart());
    }
}
