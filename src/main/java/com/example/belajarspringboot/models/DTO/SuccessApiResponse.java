package com.example.belajarspringboot.models.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SuccessApiResponse<T> {
    private int status;
    private String message;
    private T data;
}

