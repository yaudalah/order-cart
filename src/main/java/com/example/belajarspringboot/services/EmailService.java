package com.example.belajarspringboot.services;

import com.example.belajarspringboot.models.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.Locale;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Async
    public void sendOrderConfirmationToEmail(Order order, String buyerEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(buyerEmail);
        message.setSubject("Order Confirmation - Order #" + order.getId());
        message.setText(emailMessage(order).toString());
        javaMailSender.send(message);
        log.info("Order confirmation sent for order id: {}", order.getId());
    }

    private static StringBuilder emailMessage(Order order) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        final String formattedTotal = currencyFormatter.format(order.getTotal());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Thank you for your order! Your order ID is ");
        stringBuilder.append(order.getId());
        stringBuilder.append(".\n");
        stringBuilder.append("Total: ");
        stringBuilder.append(formattedTotal);
        stringBuilder.append("\n");
        stringBuilder.append("Status: ");
        stringBuilder.append(order.getStatus());
        return stringBuilder;
    }
}

