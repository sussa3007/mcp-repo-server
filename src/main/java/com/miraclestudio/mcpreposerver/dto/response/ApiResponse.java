package com.miraclestudio.mcpreposerver.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {
    private int statusCode;
    private String status;
    private T data;
    private String message;
    private Integer errorCode;

    // 성공 응답 생성 메서드
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .statusCode(200)
                .status("SUCCESS")
                .data(data)
                .message(message)
                .build();
    }

    // 성공 응답 생성 메서드 (statusCode 포함)
    public static <T> ApiResponse<T> success(T data, String message, int statusCode) {
        return ApiResponse.<T>builder()
                .statusCode(statusCode)
                .status("SUCCESS")
                .data(data)
                .message(message)
                .build();
    }

    // 실패 응답 생성 메서드
    public static <T> ApiResponse<T> error(int statusCode, String message) {
        return ApiResponse.<T>builder()
                .statusCode(statusCode)
                .status("FAILED")
                .data(null)
                .message(message)
                .build();
    }

    // 실패 응답 생성 메서드 (errorCode 포함)
    public static <T> ApiResponse<T> error(int statusCode, String message, Integer errorCode) {
        return ApiResponse.<T>builder()
                .statusCode(statusCode)
                .status("FAILED")
                .data(null)
                .message(message)
                .errorCode(errorCode)
                .build();
    }
}