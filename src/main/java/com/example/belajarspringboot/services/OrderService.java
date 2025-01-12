package com.example.belajarspringboot.services;

import com.example.belajarspringboot.Validators.OrderValidator;
import com.example.belajarspringboot.models.DTO.SuccessApiResponse;
import com.example.belajarspringboot.models.DTO.SuccessListApiResponse;
import com.example.belajarspringboot.models.Order;
import com.example.belajarspringboot.models.OrderCart;
import com.example.belajarspringboot.models.OrderItem;
import com.example.belajarspringboot.models.User;
import com.example.belajarspringboot.repositories.OrderCartRepository;
import com.example.belajarspringboot.repositories.OrderRepository;
import com.example.belajarspringboot.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.nio.file.attribute.UserPrincipal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.example.belajarspringboot.models.Constants.PLACED;

@Service
@Slf4j
public class OrderService extends BaseService<Order, Long> {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderCartRepository orderCartRepository;
    private final EmailService emailService;
    private final UserServiceImpl userService;

    public OrderService(JpaRepository<Order, Long> repository, OrderRepository orderRepository, OrderValidator orderValidator,
                        OrderCartRepository orderCartRepository, EmailService emailService, UserServiceImpl userService) {
        super(repository);
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderCartRepository = orderCartRepository;
        this.emailService = emailService;
        this.userService = userService;
    }

    @Override
    public void updateEntity(Order existingEntity, Order entityDetails) {
        existingEntity.getItems().clear();
        existingEntity.getItems().addAll(entityDetails.getItems());
    }

    public SuccessListApiResponse<Object> getOrdersByUserId(UUID userId) {
        return SuccessListApiResponse.builder()
                .status(HttpStatus.OK.value())
                .data(Collections.singletonList(orderRepository.findByUserId(userId)))
                .message(HttpStatus.OK.getReasonPhrase())
                .build();
    }

    @Transactional
    public SuccessApiResponse<Object> placeOrder() {
        try {
            final User user = userService.getUserLogin();
            OrderCart cart = orderCartRepository.findByUserUserId(user.getUserId()).orElse(new OrderCart());

            orderValidator.validateCart(cart);
            orderValidator.reduceStock(cart);
            final BigDecimal total = orderValidator.setOrderItemsTotalPrice(cart);

            List<OrderItem> orderItems = cart.getItems()
                    .stream()
                    .map(item -> OrderItem.builder()
                            .product(item.getProduct())
                            .quantity(item.getQuantity())
                            .build())
                    .toList();

            // Create the order
            Order order = Order.builder()
                    .items(orderItems)
                    .status(PLACED)
                    .total(total)
                    .userId(user.getUserId())
                    .build();
            create(order);

            // Clear the cart after placing the order
            cart.getItems().clear();
            orderCartRepository.save(cart); // Persist the empty cart
            emailService.sendOrderConfirmationToEmail(order, user.getEmail());
            // todo handle if send email failed
            return SuccessApiResponse.builder()
                    .status(HttpStatus.CREATED.value())
                    .message(HttpStatus.CREATED.getReasonPhrase())
                    .data(order)
                    .build();
        } catch (Exception e) {
            log.error("Error placing order: {}", e.getMessage(), e);
            throw new ServiceException("Failed to place order.", e);
        }
    }
}
