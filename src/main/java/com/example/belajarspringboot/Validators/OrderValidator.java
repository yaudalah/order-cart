package com.example.belajarspringboot.Validators;

import com.example.belajarspringboot.models.OrderCart;
import com.example.belajarspringboot.models.OrderItem;
import com.example.belajarspringboot.models.Product;
import com.example.belajarspringboot.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@AllArgsConstructor
public class OrderValidator {

    private ProductRepository productRepository;

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

        for (OrderItem item : cart.getItems()) {
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                throw new ServiceException("Not enough stock for product: " + product.getName());
            }
        }
    }

    public void reduceStock(OrderCart cart) {
        for (OrderItem item : cart.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }
    }
}

