package com.miraclestudio.mcpreposerver.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter
@Builder
public class ApiResponse<T> {
    private int statusCode;
    private String status;
    private T data;
    private String message;
    private Integer errorCode;
    private PageInfo pageInfo;

    // 페이지 정보를 담는 내부 클래스
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PageInfo {
        private long totalElements; // 전체 항목 수
        private int totalPages; // 전체 페이지 수
        private int pageNumber; // 현재 페이지 번호 (0부터 시작)
        private int pageSize; // 페이지 크기
        private int numberOfElements; // 현재 페이지의 항목 수
        private boolean first; // 첫 페이지 여부
        private boolean last; // 마지막 페이지 여부
        private boolean empty; // 데이터가 비어있는지 여부
    }

    // 성공 응답 생성 메서드
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .statusCode(200)
                .status("SUCCESS")
                .data(data)
                .message(message)
                .build();
    }

    // 성공 응답 생성 메서드 (상태 코드 지정)
    public static <T> ApiResponse<T> success(T data, String message, int statusCode) {
        return ApiResponse.<T>builder()
                .statusCode(statusCode)
                .status("SUCCESS")
                .data(data)
                .message(message)
                .build();
    }

    // Page 타입 데이터용 성공 응답 생성 메서드
    public static <T> ApiResponse<T> success(Page page, T data, String message) {
        PageInfo pageInfo = PageInfo.builder()
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .numberOfElements(page.getNumberOfElements())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .build();

        return ApiResponse.<T>builder()
                .statusCode(200)
                .status("SUCCESS")
                .data(data) // 실제 데이터 목록만 반환
                .pageInfo(pageInfo)
                .message(message)
                .build();
    }

    // Page 타입 데이터용 성공 응답 생성 메서드 (상태 코드 지정)
    public static <T> ApiResponse<T> success(Page page, T data, String message, int statusCode) {
        PageInfo pageInfo = PageInfo.builder()
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .numberOfElements(page.getNumberOfElements())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .build();

        return ApiResponse.<T>builder()
                .statusCode(statusCode)
                .status("SUCCESS")
                .data(data) // 실제 데이터 목록만 반환
                .pageInfo(pageInfo)
                .message(message)
                .build();
    }

    // 실패 응답 생성 메서드
    public static <T> ApiResponse<T> error(int statusCode, String message, Integer errorCode) {
        return ApiResponse.<T>builder()
                .statusCode(statusCode)
                .status("FAILED")
                .errorCode(errorCode)
                .data(null)
                .message(message)
                .build();
    }
}