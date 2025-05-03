package com.example.ordercart.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private String status;
    private String message;
    private Object data;

    @Builder.Default
    private long timestamp = Instant.now().toEpochMilli();

    // SUCCESS without data
    public static ResponseEntity<ApiResponse> ok() {
        return ResponseEntity.ok(buildSuccess("Success", null));
    }

    // SUCCESS with custom message
    public static ResponseEntity<ApiResponse> ok(String message) {
        return ResponseEntity.ok(buildSuccess(message, null));
    }

    // SUCCESS with message and data
    public static ResponseEntity<ApiResponse> ok(String message, Object data) {
        return ResponseEntity.ok(buildSuccess(message, data));
    }
    // SUCCESS with data
    public static ResponseEntity<ApiResponse> ok(Object data) {
        return ResponseEntity.ok(buildSuccess("Success", data));
    }

    // FAILED with message
    public static ResponseEntity<ApiResponse> failed(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildFailed(message));
    }

    // FAILED with custom status code
    public static ResponseEntity<ApiResponse> failed(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(buildFailed(message));
    }

    // Internal reusable builders
    private static ApiResponse buildSuccess(String message, Object data) {
        return ApiResponse.builder()
                .status("SUCCESS")
                .message(message)
                .data(data)
                .build();
    }

    private static ApiResponse buildFailed(String message) {
        return ApiResponse.builder()
                .status("FAILED")
                .message(message)
                .build();
    }
}