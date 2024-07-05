package com.example.belajarspringboot.services;

import com.example.belajarspringboot.Validators.OrderValidator;
import com.example.belajarspringboot.models.DTO.OrderCartDTO;
import com.example.belajarspringboot.models.DTO.SuccessApiResponse;
import com.example.belajarspringboot.models.DTO.SuccessListApiResponse;
import com.example.belajarspringboot.models.OrderCart;
import com.example.belajarspringboot.models.OrderItem;
import com.example.belajarspringboot.models.User;
import com.example.belajarspringboot.repositories.OrderCartRepository;
import com.example.belajarspringboot.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.UUID;

@Service
@Slf4j
public class OrderCartService extends BaseService<OrderCart, Long> {
    private final UserRepository userRepository;
    private final OrderValidator orderValidator;
    private final OrderCartRepository orderCartRepository;

    @Autowired
    public OrderCartService(OrderCartRepository orderCartRepository,
                            UserRepository userRepository,
                            OrderValidator orderValidator) {
        super(orderCartRepository);
        this.userRepository = userRepository;
        this.orderValidator = orderValidator;
        this.orderCartRepository = orderCartRepository;
    }

    @Override
    protected void updateEntity(OrderCart existingEntity, OrderCart entityDetails) {
        existingEntity.getItems().clear();
        existingEntity.getItems().addAll(entityDetails.getItems());
    }

    @Transactional
    public SuccessApiResponse<Object> addProductToCart(OrderItem orderItem) {
        try {
            final User user = getUserLogin();

            OrderCart cart = ((OrderCartRepository) repository).findByUserUserId(user.getUserId())
                    .orElse(new OrderCart());
            cart.setUser(user);

            orderValidator.validateAndAddProductToCart(orderItem, cart);
            return create(cart);
        } catch (ServiceException e) {
            log.error("Error placing order: {}", e.getMessage(), e);
            throw new ServiceException("Failed to add product to cart.", e);
        }
    }

    private User getUserLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public SuccessListApiResponse<Object> getCartByUserId(UUID userId) {
        try {
            return SuccessListApiResponse.builder()
                    .status(HttpStatus.OK.value())
                    .data(Collections.singletonList(orderValidator.fetchOrderCartByUserId(userId)))
                    .message(HttpStatus.OK.getReasonPhrase())
                    .build();
        } catch (ServiceException e) {
            log.error("Error retrieving order cart.", e);
            throw new ServiceException("Failed to get cart.", e);
        }
    }

}

