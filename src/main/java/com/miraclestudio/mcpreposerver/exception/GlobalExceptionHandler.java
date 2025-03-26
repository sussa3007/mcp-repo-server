package com.miraclestudio.mcpreposerver.exception;

import com.miraclestudio.mcpreposerver.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 예외 처리
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException e) {
        log.error("Business Exception: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(getHttpStatus(errorCode))
                .body(ApiResponse.error(errorCode.getCode(), e.getMessage(), errorCode.getCode()));
    }

    /**
     * 유효성 검증 예외 처리 (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        log.error("Validation Exception: {}", e.getMessage());
        String errorMessage = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ErrorCode.INVALID_INPUT_VALUE.getCode(), errorMessage,
                        ErrorCode.INVALID_INPUT_VALUE.getCode()));
    }

    /**
     * 유효성 검증 예외 처리 (Binding Error)
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ApiResponse<Object>> handleBindException(BindException e) {
        log.error("Binding Exception: {}", e.getMessage());
        String errorMessage = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ErrorCode.INVALID_INPUT_VALUE.getCode(), errorMessage,
                        ErrorCode.INVALID_INPUT_VALUE.getCode()));
    }

    /**
     * 제약 조건 위반 예외 처리
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ApiResponse<Object>> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("Constraint Violation Exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ErrorCode.INVALID_INPUT_VALUE.getCode(), e.getMessage(),
                        ErrorCode.INVALID_INPUT_VALUE.getCode()));
    }

    /**
     * 지원하지 않는 HTTP 메소드 예외 처리
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ApiResponse<Object>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        log.error("Method Not Allowed Exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.error(ErrorCode.METHOD_NOT_ALLOWED.getCode(), e.getMessage(),
                        ErrorCode.METHOD_NOT_ALLOWED.getCode()));
    }

    /**
     * 메소드 인자 타입 불일치 예외 처리
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApiResponse<Object>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        log.error("Type Mismatch Exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ErrorCode.INVALID_TYPE_VALUE.getCode(), e.getMessage(),
                        ErrorCode.INVALID_TYPE_VALUE.getCode()));
    }

    /**
     * HTTP 메시지 변환 실패 예외 처리
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        log.error("Message Not Readable Exception: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ErrorCode.INVALID_INPUT_VALUE.getCode(), "요청 본문을 읽을 수 없습니다.",
                        ErrorCode.INVALID_INPUT_VALUE.getCode()));
    }

    /**
     * 기타 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        log.error("Internal Server Error: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), "서버 오류가 발생했습니다.",
                        ErrorCode.INTERNAL_SERVER_ERROR.getCode()));
    }

    /**
     * 에러 코드에 따른 HTTP 상태 코드 결정
     */
    private HttpStatus getHttpStatus(ErrorCode errorCode) {
        int code = errorCode.getCode();

        // 에러 코드 범위별로 HTTP 상태 코드 매핑
        if (code >= 1000 && code < 2000) {
            return code == 1001 ? HttpStatus.METHOD_NOT_ALLOWED : HttpStatus.BAD_REQUEST;
        } else if (code >= 2000 && code < 3000) {
            return code == 2000 ? HttpStatus.UNAUTHORIZED : HttpStatus.FORBIDDEN;
        } else if (code >= 3000 && code < 6000) {
            return HttpStatus.NOT_FOUND;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }
}