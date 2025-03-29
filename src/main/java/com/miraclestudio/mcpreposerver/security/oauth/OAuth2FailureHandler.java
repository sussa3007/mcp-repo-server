package com.miraclestudio.mcpreposerver.security.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miraclestudio.mcpreposerver.dto.response.ApiResponse;
import com.miraclestudio.mcpreposerver.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;
    private final OAuth2Properties oAuth2Properties;

    @Value("${app.oauth2.failure-redirect-url}")
    private String failureRedirectUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        log.error("OAuth2 인증 실패: {}", exception.getMessage());
        ErrorCode errorCode = getErrorCode(exception);
        String redirectUrl = String.format("%s?error=%s&message=%s",
                failureRedirectUrl,
                errorCode.getStringErrorCode(),
                exception.getMessage());
        response.sendRedirect(redirectUrl);
    }

    private ErrorCode getErrorCode(AuthenticationException exception) {
        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
            return switch (error.getErrorCode()) {
                case "invalid_token" -> ErrorCode.INVALID_TOKEN;
                case "invalid_request" -> ErrorCode.INVALID_REQUEST;
                case "unauthorized_client" -> ErrorCode.UNAUTHORIZED_CLIENT;
                case "access_denied" -> ErrorCode.ACCESS_DENIED;
                case "USER_ALREADY_EXISTS" -> ErrorCode.USER_ALREADY_EXISTS;
                default -> ErrorCode.OAUTH2_AUTHENTICATION_ERROR;
            };
        }
        return ErrorCode.AUTHENTICATION_FAILED;
    }

    private String getErrorMessage(AuthenticationException exception) {
        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
            return String.format("OAuth2 인증 오류: %s", error.getDescription());
        }
        return "소셜 로그인 중 오류가 발생했습니다.";
    }

    private boolean isRedirectRequired(HttpServletRequest request) {
        String redirectUri = request.getParameter("redirect_uri");
        return redirectUri != null && !redirectUri.isEmpty();
    }
}