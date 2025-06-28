package com.example.ordercart.service;

import com.example.ordercart.entity.Invoice;
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

import static com.example.ordercart.common.constant.InvoiceConstant.ORDER_INVOICE_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${invoice.date.pattern}")
    private String invoiceDatePattern;

    @Async
    public void sendOrderConfirmationToEmail(Invoice invoice, String buyerEmail, String emailSubject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(buyerEmail);
        message.setSubject(getEmailSubject(invoice));
        message.setText(emailMessage(invoice).toString());
        javaMailSender.send(message);
        log.info("Invoice confirmation sent for invoice id: {}", invoice.getId());
    }

    private String getEmailSubject(Invoice invoice) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(invoiceDatePattern);

        // Example: INV-13124 and todo this should be save in table as a refId and save it into the db
        String formattedDate = now.format(formatter);
        return ORDER_INVOICE_PREFIX + invoice.getId() + formattedDate;
    }

    private static StringBuilder emailMessage(Invoice invoice) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.of("id", "ID"));
        final String formattedTotal = currencyFormatter.format(invoice.getTotalPrice());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Thank you for your invoice! Your invoice ID is ");
        stringBuilder.append(invoice.getId()); // todo refactor use refId that saved into the DB
        stringBuilder.append(".\n");
        stringBuilder.append("Total: ");
        stringBuilder.append(formattedTotal);
        stringBuilder.append("\n");
        stringBuilder.append("Status: ");
        stringBuilder.append(invoice.getStatus());
        return stringBuilder;
    }
}

