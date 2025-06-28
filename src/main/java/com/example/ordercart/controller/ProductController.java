package com.example.ordercart.controller;

import com.example.ordercart.model.request.ProductRequest;
import com.example.ordercart.model.response.ApiResponse;
import com.example.ordercart.model.response.ProductResponse;
import com.example.ordercart.security.annotation.CheckUserPermission;
import com.example.ordercart.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.example.ordercart.common.constant.UrlPathsConstant.PRODUCT;
import static com.example.ordercart.common.constant.UrlPathsConstant.URL_PREFIX_V1;

@Slf4j
@RestController
@RequestMapping(URL_PREFIX_V1 + PRODUCT)
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse> fetchAllProduct(Pageable pageable,
                                                       @RequestParam(required = false, defaultValue = "") String username) {
        return ApiResponse.ok(productService.getAllProducts(pageable, username));
    }

    @PostMapping
    @CheckUserPermission
    public ResponseEntity<ApiResponse> createProduct(@RequestBody ProductRequest productRequest) {
        log.info("[ProductController] product payload: {}", productRequest);
        ProductResponse productResponse = productService.createProduct(productRequest);
        return ApiResponse.ok(productResponse);
    }

    @PatchMapping
    @CheckUserPermission
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductRequest productRequest,
                                                     @RequestParam UUID productId,
                                                     @RequestParam(required = false, defaultValue = "") String username) {
        log.info("[ProductController] username: {} product payload: {}", username, productRequest);
        ProductResponse productResponse = productService.updateProduct(productId, productRequest, username);
        return ApiResponse.ok(productResponse);
    }
}
