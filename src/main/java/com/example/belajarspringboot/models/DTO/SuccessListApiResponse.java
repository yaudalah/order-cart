package com.example.belajarspringboot.models.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SuccessListApiResponse<T> {
    private int status;
    private String message;
    private List<T> data;
}
