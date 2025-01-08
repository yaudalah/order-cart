package com.example.belajarspringboot;

import com.example.belajarspringboot.Validators.OrderValidator;
import com.example.belajarspringboot.models.DTO.SuccessApiResponse;
import com.example.belajarspringboot.models.DTO.SuccessListApiResponse;
import com.example.belajarspringboot.models.Order;
import com.example.belajarspringboot.models.OrderCart;
import com.example.belajarspringboot.models.OrderItem;
import com.example.belajarspringboot.models.User;
import com.example.belajarspringboot.repositories.OrderCartRepository;
import com.example.belajarspringboot.repositories.OrderRepository;
import com.example.belajarspringboot.services.EmailService;
import com.example.belajarspringboot.services.OrderService;
import com.example.belajarspringboot.services.UserServiceImpl;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderValidator orderValidator;
    @Mock
    private OrderCartRepository orderCartRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateEntity() {
        Order existingOrder = new Order();
        existingOrder.setItems(new ArrayList<>());
        Order newOrderDetails = new Order();
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem());
        newOrderDetails.setItems(items);

        orderService.updateEntity(existingOrder, newOrderDetails);

        assertEquals(1, existingOrder.getItems().size());
    }

    @Test
    void testGetOrdersByUserId() {
        UUID userId = UUID.randomUUID();
        when(orderRepository.findByUserId(userId)).thenReturn(new ArrayList<>());

        SuccessListApiResponse<Object> response = orderService.getOrdersByUserId(userId);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertInstanceOf(List.class, response.getData());
    }

    @Test
    void testPlaceOrder_Success() {
        // Mock User
        final String email = "testuser@gmail.com";
        final String username = "testuser";
        User mockUser = new User();
        mockUser.setUserId(UUID.randomUUID());
        mockUser.setEmail(email);

        // Mock SecurityContext and Authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);

        // Mock user login
        when(userService.getUserLogin()).thenReturn(mockUser);

        // Mock Cart
        OrderItem mockItem = new OrderItem();
        List<OrderItem> mockItems = new ArrayList<>();
        mockItems.add(mockItem);

        OrderCart mockCart = new OrderCart();
        mockCart.setItems(mockItems);

        when(orderCartRepository.findByUserUserId(mockUser.getUserId())).thenReturn(Optional.of(mockCart));

        // Mock OrderValidator
        doNothing().when(orderValidator).validateCart(mockCart);
        doNothing().when(orderValidator).reduceStock(mockCart);
        when(orderValidator.setOrderItemsTotalPrice(mockCart)).thenReturn(BigDecimal.TEN);

        // Execute and Assert
        SuccessApiResponse<Object> response = orderService.placeOrder();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        verify(orderCartRepository).save(mockCart);
        verify(emailService).sendOrderConfirmationToEmail(any(), eq(mockUser.getEmail()));
    }

    @Test
    void testPlaceOrder_Failure() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        when(userService.getUserLogin()).thenThrow(new UsernameNotFoundException("User not found"));
        assertThrows(ServiceException.class, () -> orderService.placeOrder());
    }
}