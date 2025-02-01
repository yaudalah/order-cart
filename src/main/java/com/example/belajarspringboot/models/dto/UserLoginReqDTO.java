package com.example.belajarspringboot.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginReqDTO {
    private String username;
    private String password;
}
