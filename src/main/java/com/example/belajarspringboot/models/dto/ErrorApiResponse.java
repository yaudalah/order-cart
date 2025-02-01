package com.example.belajarspringboot.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorApiResponse {
    private int status;
    private String message;
    private String timestamp;
}

