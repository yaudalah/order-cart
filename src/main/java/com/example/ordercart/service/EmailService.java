package com.example.ordercart.service;

import com.example.ordercart.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    public static final String ORDER_INVOICE_PREFIX = "ORD-";
    private final JavaMailSender javaMailSender;

    @Value("${order.date.pattern}")
    private String orderDatePattern;

    @Async
    public void sendOrderConfirmationToEmail(Order order, String buyerEmail, String refId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(buyerEmail);
        message.setSubject(getEmailSubject(order));
        message.setText(emailMessage(order).toString());
        javaMailSender.send(message);
        log.info("Order confirmation sent for order id: {}", order.getId());
    }

    private String getEmailSubject(Order order) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(orderDatePattern);

        String formattedDate = now.format(formatter);
        return ORDER_INVOICE_PREFIX + order.getId() + formattedDate; // Example: ORD-13124 and todo this should be save in table as a refId and save it into the db
    }

    private static StringBuilder emailMessage(Order order) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.of("id", "ID"));
        final String formattedTotal = currencyFormatter.format(order.getTotalPrice());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Thank you for your order! Your order ID is ");
        stringBuilder.append(order.getId()); // todo refactor use refId that saved into the DB
        stringBuilder.append(".\n");
        stringBuilder.append("Total: ");
        stringBuilder.append(formattedTotal);
        stringBuilder.append("\n");
        stringBuilder.append("Status: ");
        stringBuilder.append(order.getStatus());
        return stringBuilder;
    }
}

