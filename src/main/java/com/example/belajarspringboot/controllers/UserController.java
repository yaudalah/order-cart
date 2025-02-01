package com.example.belajarspringboot.controllers;

import com.example.belajarspringboot.models.dto.SuccessApiResponse;
import com.example.belajarspringboot.models.dto.UserLoginReqDTO;
import com.example.belajarspringboot.models.User;
import com.example.belajarspringboot.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("register")
    public SuccessApiResponse<Object> register(@Valid @RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("login")
    public SuccessApiResponse<Object> login(@Valid @RequestBody UserLoginReqDTO user) {
        return userService.login(user);
    }

}
