package com.example.belajarspringboot.middlewares;

import com.example.belajarspringboot.models.DTO.ErrorApiResponse;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class GlobalExceptionHandler {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            .withZone(ZoneId.of("Asia/Jakarta"));
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorApiResponse> handleServiceException(ServiceException ex) {
        ErrorApiResponse errorApiResponse = new ErrorApiResponse();
        errorApiResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorApiResponse.setMessage(ex.getMessage());
        errorApiResponse.setTimestamp(formatter.format(Instant.now()));
        return new ResponseEntity<>(errorApiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorApiResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ErrorApiResponse errorApiResponse = new ErrorApiResponse();
        errorApiResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorApiResponse.setMessage(ex.getMessage());
        errorApiResponse.setTimestamp(formatter.format(Instant.now()));
        return new ResponseEntity<>(errorApiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApiResponse> handleValidationException(MethodArgumentNotValidException ex) {
        final String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ErrorApiResponse errorApiResponse = new ErrorApiResponse();
        errorApiResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorApiResponse.setMessage(message);
        errorApiResponse.setTimestamp(formatter.format(Instant.now()));
        return new ResponseEntity<>(errorApiResponse, HttpStatus.BAD_REQUEST);
    }
}
