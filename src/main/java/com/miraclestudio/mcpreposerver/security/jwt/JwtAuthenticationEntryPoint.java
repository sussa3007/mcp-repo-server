package com.miraclestudio.mcpreposerver.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miraclestudio.mcpreposerver.dto.response.ApiResponse;
import com.miraclestudio.mcpreposerver.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse<?> errorResponse = ApiResponse.error(
                HttpServletResponse.SC_FORBIDDEN,
                authException.getMessage(),
                ErrorCode.ACCESS_DENIED.getCode()); // JWT 인증 실패 에러 코드

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}