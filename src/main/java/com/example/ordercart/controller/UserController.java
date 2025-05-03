package com.example.ordercart.controller;

import com.example.ordercart.entity.User;
import com.example.ordercart.model.dto.UserDto;
import com.example.ordercart.model.request.UserLoginReq;
import com.example.ordercart.model.response.ApiResponse;
import com.example.ordercart.service.CsvExportService;
import com.example.ordercart.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final CsvExportService csvExportService;

    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        userService.register(user);
        return ApiResponse.ok();
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody UserLoginReq request) {
        return ApiResponse.ok(userService.login(request));
    }

    @PostMapping("export")
    public ResponseEntity<StreamingResponseBody> exportEmployeesToCsv(@Valid @RequestBody UserDto userDTO) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(outputStream -> csvExportService.exportEmployeesToCsv(outputStream, userDTO));
    }

}
