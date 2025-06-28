package com.example.ordercart.exception;

import com.example.ordercart.model.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public static ResponseEntity<ApiResponse> handleValidationException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ApiResponse.failed(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public static ResponseEntity<ApiResponse> handleIllegalArgs(IllegalArgumentException ex) {
        return handleValidationException(ex);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public static ResponseEntity<ApiResponse> usernameNotFoundException(UsernameNotFoundException ex) {
        return handleValidationException(ex);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public static ResponseEntity<ApiResponse> expiredJwtException(Exception ex) {
        return handleValidationException(ex);
    }
}
