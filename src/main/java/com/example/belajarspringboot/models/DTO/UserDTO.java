package com.example.belajarspringboot.models.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO {
    private String username;
    private String email;
    private String phoneNumber;
}
