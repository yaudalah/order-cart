package com.example.belajarspringboot.models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResDTO {
    @NotBlank
    private String token;

    @NotBlank(message = "Username Could Not be Empty!")
    private String username;
}
