package com.example.ordercart.exception;

import com.example.ordercart.model.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public static ApiResponse handleValidationException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ApiResponse.failed(ex.getMessage()).getBody();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public static ApiResponse handleIllegalArgs(IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);
        return ApiResponse.failed("Invalid input: " + ex.getMessage()).getBody();
    }
}
