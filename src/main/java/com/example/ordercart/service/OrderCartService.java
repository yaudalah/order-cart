package com.example.ordercart.service;

import com.example.ordercart.entity.Invoice;
import com.example.ordercart.entity.OrderCart;
import com.example.ordercart.entity.OrderItem;
import com.example.ordercart.entity.Product;
import com.example.ordercart.entity.User;
import com.example.ordercart.exception.ServiceException;
import com.example.ordercart.repository.InvoiceRepository;
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

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static com.example.ordercart.common.constant.InvoiceConstant.DECLINED_STATUS;
import static com.example.ordercart.common.constant.InvoiceConstant.ORDER_INVOICE_PREFIX;
import static com.example.ordercart.common.constant.InvoiceConstant.PAID_STATUS;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCartService {
    private final OrderCartRepository orderCartRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InvoiceRepository invoiceRepository;

    private final UserService userService;
    private final EmailService emailService;

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


    @Transactional
    public String clearOrderCart() {
        UUID userId = userService
                .fetchUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .getId();

        OrderCart cart = orderCartRepository.findOrderCartByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Order cart not found for user"));

        long deleted = orderItemRepository.deleteAllByCartId(cart.getId());
        return "Order cart cleared successfully (" + deleted + " items removed)";
    }

    @Transactional
    public String checkoutOrderCart() {
        // 1) Identify current user
        UUID userId = userService
                .fetchUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .getId();

        // 2) Find their cart and basic validations
        OrderCart cart = orderCartRepository.findOrderCartByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Order cart not found for user"));

        // 3) Load items in the cart (expects a repository method findAllByCartId)
        //    If you don't have this method, add it to OrderItemRepository: List<OrderItem> findAllByCartId(UUID cartId);
        List<OrderItem> items = orderItemRepository.findAllByCartId(cart.getId());
        if (items == null || items.isEmpty()) {
            throw new ServiceException("Cannot checkout an empty cart");
        }

        // 4) Validate each item and compute total
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            if (item.getProduct() == null) {
                throw new ServiceException("Cart contains an item with no product reference");
            }
            if (item.getQuantity() <= 0) {
                throw new ServiceException("Invalid quantity for product: " + item.getProduct().getId());
            }

            // Re-load product to ensure it still exists and is not deleted/disabled
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found during checkout: " + item.getProduct().getId()));

            if (product.getPrice() == null) {
                throw new ServiceException("Product has no price set: " + product.getId());
            }

            BigDecimal line = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(line);
        }

        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("Total amount must be greater than zero");
        }

        // 5) "Connect" to a free sandbox endpoint (httpbin) to simulate a payment gateway call
        //    This is for learning/demo only. Replace with a real gateway SDK or API later.
        boolean paid;
        try {
            paid = this.processPaymentViaSandbox(total, "IDR", "OrderCart-Checkout-" + cart.getId());
            final Invoice invoice = Invoice.builder()
                    .userId(userId)
                    .refId(ORDER_INVOICE_PREFIX + cart.getId())
                    .items(items)
                    .status(paid ? PAID_STATUS : DECLINED_STATUS)
                    .totalPrice(total)
                    .build();
            this.invoiceRepository.save(invoice);
            this.emailService.sendOrderConfirmationToEmail(
                    invoice,
                    userService.fetchUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getEmail(),
                    "Order Cart Payment Confirmation"
            );
        } catch (Exception e) {
            log.error("[OrderCartService] Payment call failed: {}", e.getMessage());
            throw new ServiceException("Payment processing failed", e);
        }

        if (!paid) {
            throw new ServiceException("Payment declined by sandbox gateway");
        }

        // 6) Clear the cart upon successful payment
        long removed = orderItemRepository.deleteAllByCartId(cart.getId());
        log.info("[OrderCartService] Checkout successful. {} items removed from cart {}", removed, cart.getId());

        return "Checkout successful. Paid IDR " + total + " (" + removed + " items).";
    }

    /**
     * Simulates a payment gateway charge by POSTing to a public sandbox endpoint (httpbin.org).
     * This is for learning/demo purposes only and does not move real money.
     */
    private boolean processPaymentViaSandbox(BigDecimal amount, String currency, String description) throws Exception {
        String body = "{\"amount\":" + amount + ",\"currency\":\"" + currency + "\",\"description\":\"" + description + "\"}";

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(5))
                .build()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://httpbin.org/post"))
                    .version(HttpClient.Version.HTTP_2)
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        // Treat HTTP 200 as an approved transaction in this sandbox demo
        boolean ok = response.statusCode() == 200;
        if (!ok) {
            log.warn("[OrderCartService] Sandbox payment not approved. Status: {}, Body: {}", response.statusCode(), response.body());
        } else {
            log.info("[OrderCartService] Sandbox payment approved. Amount: {} {}", currency, amount);
        }
        return ok;
    }
}