package com.example.ordercart.service;

import com.example.ordercart.entity.OrderCart;
import com.example.ordercart.entity.OrderItem;
import com.example.ordercart.entity.Product;
import com.example.ordercart.entity.User;
import com.example.ordercart.exception.ServiceException;
import com.example.ordercart.repository.OrderCartRepository;
import com.example.ordercart.repository.OrderItemRepository;
import com.example.ordercart.repository.ProductRepository;
import com.example.ordercart.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCartService {
    private final OrderCartRepository orderCartRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    @Transactional
    public String addProductsToCart(OrderItem orderItem) {
        log.info("[OrderCartService] Checking if order item is valid: {}", orderItem);
        if (orderItem == null || orderItem.getProduct() == null || orderItem.getQuantity() <= 0) {
            throw new IllegalArgumentException("Invalid order item details provided");
        }

        final UUID userId = userService.fetchUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        final User userProxy = userRepository.getReferenceById(userId);

        OrderCart cart = orderCartRepository
                .findOrderCartByUserId(userId)
                .orElseGet(() -> {
                    log.info("[OrderCartService] No existing cart found for user {}, creating a new one", userId);
                    return orderCartRepository.save(
                            OrderCart.builder()
                                    .user(userProxy)
                                    .build()
                    );
                });

        Product product = productRepository.findById(orderItem.getProduct().getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        OrderItem item = OrderItem.builder()
                .orderCartId(cart.getId())
                .product(product)
                .quantity(orderItem.getQuantity())
                .build();

        try {
            log.info("[OrderCartService] START Adding item to cart: {}", item);
            orderItemRepository.save(item);
        } catch (Exception e) {
            log.error("[OrderCartService] ERROR while adding item to cart: {}", e.getMessage());
            throw new ServiceException(e.getMessage(), e);
        } finally {
            log.info("[OrderCartService] END Adding item to cart");
        }

        return "Item added successfully";
    }

    public String getOrderCart(Pageable pageable) {
        return "";
    }

    public String removeProductFromCart(List<UUID> productId) {
        return "Product removed successfully";
    }

    public String clearOrderCart() {
        final UUID userId = userService.fetchUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        OrderCart cart = orderCartRepository
                .findOrderCartByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Order cart not found for user"));

        // TODO: Consider using a bulk delete operation for better performance and also handle cascade delete
//        orderItemRepository.deleteAllByOrderCartId(cart.getId());
        return "Order cart cleared successfully";
    }

    public String checkoutOrderCart() {
        return null;
    }
}
