package com.example.belajarspringboot.models.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Builder
public class UserResDTO {
    private String token;
    private String username;
}
