package com.miraclestudio.mcpreposerver.dto.response;

import com.miraclestudio.mcpreposerver.domain.user.Role;
import com.miraclestudio.mcpreposerver.domain.user.SocialType;
import com.miraclestudio.mcpreposerver.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
@Getter
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private String imageUrl;
    private SocialType socialType;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .imageUrl(user.getImageUrl())
                .socialType(user.getSocialType())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}