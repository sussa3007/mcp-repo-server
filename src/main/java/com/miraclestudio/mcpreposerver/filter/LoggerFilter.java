package com.miraclestudio.mcpreposerver.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LoggerFilter extends OncePerRequestFilter {

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!request.getRequestURI().startsWith("/api/v1/")) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            if (isDevelopmentProfile()) {
                logRequest(requestWrapper, duration);
            } else {
                log.info("[API] {} {} ({}ms)", request.getMethod(), request.getRequestURI(), duration);
            }

            responseWrapper.copyBodyToResponse();
        }
    }

    private boolean isDevelopmentProfile() {
        return activeProfile.contains("dev") || activeProfile.contains("default");
    }

    private void logRequest(ContentCachingRequestWrapper request, long duration) {
        String headers = Collections.list(request.getHeaderNames())
                .stream()
                .map(headerName -> headerName + ": " + request.getHeader(headerName))
                .collect(Collectors.joining("\n"));

        String requestBody = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);

        log.info("""
                [API Request] {} {} ({}ms)
                Headers:
                {}
                Body:
                {}
                """,
                request.getMethod(),
                request.getRequestURI(),
                duration,
                headers,
                requestBody);
    }
}
