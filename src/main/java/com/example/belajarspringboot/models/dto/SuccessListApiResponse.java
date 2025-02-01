package com.example.belajarspringboot.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
public class SuccessListApiResponse<T> {
    private int status;
    private String message;
    private List<T> data;

    @Builder.Default
    private Instant timestamp = Instant.now();
}
