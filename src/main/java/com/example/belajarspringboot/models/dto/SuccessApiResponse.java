package com.example.belajarspringboot.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class SuccessApiResponse<T> {
    private int status;
    private String message;
    private T data;

    @Builder.Default
    private Instant timestamp = Instant.now();
}

