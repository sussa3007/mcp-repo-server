package com.miraclestudio.mcpreposerver.controller.auth;

import com.miraclestudio.mcpreposerver.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "인증 관련 API")
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    /**
     * GitHub 로그인 URL을 반환합니다.
     * 프론트엔드에서는 이 URL로 리다이렉트하여 GitHub 로그인을 시작합니다.
     */
    @Operation(summary = "GitHub 로그인 URL 조회", description = "GitHub OAuth2 로그인을 위한 URL을 반환합니다.")
    @GetMapping("/github/url")
    public ApiResponse<String> getGithubLoginUrl() {
        String githubLoginUrl = "/api/v1/oauth2/authorization/github";
        return ApiResponse.success(githubLoginUrl, "GitHub 로그인 URL이 성공적으로 반환되었습니다.");
    }

    /**
     * Google 로그인 URL을 반환합니다.
     * 프론트엔드에서는 이 URL로 리다이렉트하여 Google 로그인을 시작합니다.
     */
    @Operation(summary = "Google 로그인 URL 조회", description = "Google OAuth2 로그인을 위한 URL을 반환합니다.")
    @GetMapping("/google/url")
    public ApiResponse<String> getGoogleLoginUrl() {
        String googleLoginUrl = "/api/v1/oauth2/authorization/google";
        return ApiResponse.success(googleLoginUrl, "Google 로그인 URL이 성공적으로 반환되었습니다.");
    }
}