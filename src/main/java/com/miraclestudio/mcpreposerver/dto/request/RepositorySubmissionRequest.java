package com.miraclestudio.mcpreposerver.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 레포지토리 제출 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepositorySubmissionRequest {

    @NotBlank(message = "레포지토리 소유자는 필수입니다.")
    private String owner;

    @NotBlank(message = "레포지토리 이름은 필수입니다.")
    private String repo;

    @NotBlank(message = "GitHub URL은 필수입니다.")
    @Pattern(regexp = "^https://github\\.com/[\\w.-]+/[\\w.-]+$", message = "유효한 GitHub URL 형식이 아닙니다.")
    private String githubUrl;

    @NotBlank(message = "레포지토리 타입은 필수입니다.")
    @Pattern(regexp = "^(server|client)$", message = "레포지토리 타입은 'server' 또는 'client'만 가능합니다.")
    private String type;

    private String description;

    private String demoUrl;

    @NotEmpty(message = "하나 이상의 태그가 필요합니다.")
    private List<String> tags;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    private String name;
}