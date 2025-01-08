package com.example.belajarspringboot.Validators;

import com.example.belajarspringboot.models.DTO.OrderCartDTO;
import com.example.belajarspringboot.models.OrderCart;
import com.example.belajarspringboot.models.OrderItem;
import com.example.belajarspringboot.models.Product;
import com.example.belajarspringboot.repositories.OrderCartRepository;
import com.example.belajarspringboot.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderValidator {

    private final ProductRepository productRepository;
    private final OrderCartRepository orderCartRepository;

    public void validateAndAddProductToCart(OrderItem orderItem, OrderCart cart) {
        Optional<Product> productOpt = productRepository.findById(orderItem.getProduct().getId());
        if (!productOpt.isPresent()) {
            throw new ServiceException("Product not found with id: " + orderItem.getProduct().getId());
        }

        Product product = productOpt.get();
        orderItem.setProduct(product);
        cart.getItems().add(orderItem);
    }

    public BigDecimal setOrderItemsTotalPrice(OrderCart cart) {
        return cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void validateCart(OrderCart cart) {
        if (cart.getItems().isEmpty()) {
            throw new ServiceException("Cart is empty, cannot place order.");
        }

        cart.getItems().forEach(item -> {
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                throw new ServiceException("Not enough stock for product: " + product.getName());
            }
        });
    }

    public void reduceStock(OrderCart cart) {
        for (OrderItem item : cart.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }
    }

    public OrderCartDTO fetchOrderCartByUserId(UUID userId) {
        OrderCart orderCart = orderCartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ServiceException("Cart not found"));
        OrderCartDTO orderCartDTO = new OrderCartDTO();
        orderCartDTO.setId(orderCart.getId());
        orderCartDTO.setUserId(orderCart.getUser().getUserId());
        orderCartDTO.setItems(orderCart.getItems());
        return orderCartDTO;
    }
}

