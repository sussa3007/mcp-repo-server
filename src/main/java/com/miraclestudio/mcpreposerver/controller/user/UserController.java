package com.miraclestudio.mcpreposerver.controller.user;

import com.miraclestudio.mcpreposerver.dto.response.ApiResponse;
import com.miraclestudio.mcpreposerver.dto.response.UserResponse;
import com.miraclestudio.mcpreposerver.service.user.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import com.miraclestudio.mcpreposerver.security.jwt.JwtTokenProvider.CustomUserDetails;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @Operation(summary = "현재 사용자 정보 조회", description = "로그인한 사용자의 정보를 반환합니다.")
    @GetMapping(
        "/me"
    )
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponse userResponse = userService.findByEmail(userDetails.getEmail());
        return ResponseEntity.ok(ApiResponse.success(userResponse, "Successfully retrieved user information."));
    }
}
