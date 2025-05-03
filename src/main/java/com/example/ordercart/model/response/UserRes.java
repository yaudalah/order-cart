package com.example.ordercart.model.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserRes {
    @NotBlank
    private String token;

    @NotBlank(message = "Username Could Not be Empty!")
    private String username;
}
