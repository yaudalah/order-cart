package com.example.belajarspringboot.models.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
public class SuccessListApiResponse<T> {
    private int status;
    private String message;
    private List<T> data;
}
