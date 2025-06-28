package com.example.ordercart.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private UUID id;
    @NotBlank(message = "The username must not be empty.")
    private String username;
    @Email
    @Size(min = 5, message = "Email minimum has to be greater than 5 characters.")
    private String email;
    @Size(min = 11, max = 13, message = "Phone Number minimum has to be greater than 11 and maximum 13 characters.")
    @NotNull(message = "Phone Number is Mandatory")
    private String phoneNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private String joinDate;
    private List<String> exportedColumn;
}
