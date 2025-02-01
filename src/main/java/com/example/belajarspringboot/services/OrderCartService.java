package com.example.belajarspringboot.services;

import com.example.belajarspringboot.Validators.OrderValidator;
import com.example.belajarspringboot.models.dto.OrderCartDTO;
import com.example.belajarspringboot.models.dto.SuccessApiResponse;
import com.example.belajarspringboot.models.dto.SuccessListApiResponse;
import com.example.belajarspringboot.models.OrderCart;
import com.example.belajarspringboot.models.OrderItem;
import com.example.belajarspringboot.models.User;
import com.example.belajarspringboot.repositories.OrderCartRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class OrderCartService extends BaseService<OrderCart, Long> {

    private final OrderValidator orderValidator;
    private final UserServiceImpl userService;

    @Autowired
    public OrderCartService(OrderCartRepository orderCartRepository,
                            OrderValidator orderValidator,
                            UserServiceImpl userService) {
        super(orderCartRepository);
        this.orderValidator = orderValidator;
        this.userService = userService;
    }

    @Override
    protected void updateEntity(OrderCart existingEntity, OrderCart entityDetails) {
        existingEntity.getItems().clear();
        existingEntity.getItems().addAll(entityDetails.getItems());
    }

    @Transactional
    public SuccessApiResponse<Object> addProductToCart(OrderItem orderItem) {
        try {
            final User user = userService.getUserLogin();

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

    public SuccessListApiResponse<Object> getCartByUserId(String userId) {
        try {
            final List<Object> list = Collections.singletonList(orderValidator.fetchOrderCartByUserId(userId));
            return SuccessListApiResponse.builder()
                    .status(HttpStatus.OK.value())
                    .data(list)
                    .message(HttpStatus.OK.getReasonPhrase())
                    .build();
        } catch (ServiceException e) {
            log.error("Error retrieving order cart.", e);
            throw new ServiceException("Failed to get cart.", e);
        }
    }

}

