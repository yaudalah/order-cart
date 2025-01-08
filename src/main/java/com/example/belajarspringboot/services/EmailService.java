package com.example.belajarspringboot.services;

import com.example.belajarspringboot.models.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${order.date.pattern}")
    private String datePattern;

    @Async
    public void sendOrderConfirmationToEmail(Order order, String buyerEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(buyerEmail);
        message.setSubject(getEmailSubject(order));
        message.setText(emailMessage(order).toString());
        javaMailSender.send(message);
        log.info("Order confirmation sent for order id: {}", order.getId());
    }

    private String getEmailSubject(Order order) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);

        String formattedDate = now.format(formatter);
        return "ORD-" + order.getId() + formattedDate; // Example: ORD-13124 and todo this should be save in table as a refId
    }

    private static StringBuilder emailMessage(Order order) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        final String formattedTotal = currencyFormatter.format(order.getTotal());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Thank you for your order! Your order ID is ");
        stringBuilder.append(order.getId()); // todo refactor use refId
        stringBuilder.append(".\n");
        stringBuilder.append("Total: ");
        stringBuilder.append(formattedTotal);
        stringBuilder.append("\n");
        stringBuilder.append("Status: ");
        stringBuilder.append(order.getStatus());
        return stringBuilder;
    }
}

