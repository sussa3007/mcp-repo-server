package com.miraclestudio.mcpreposerver.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(description = "프로젝트 제출 요청")
public class SubmissionRequest {

    @NotBlank(message = "프로젝트 이름은 필수입니다.")
    @Schema(description = "프로젝트 이름", example = "My MCP Client")
    private String name;

    @NotBlank(message = "작성자 이름은 필수입니다.")
    @Schema(description = "작성자", example = "홍길동")
    private String author;

    @NotBlank(message = "레포지토리 타입은 필수입니다.")
    @Pattern(regexp = "^(server|client)$", message = "레포지토리 타입은 server 또는 client만 가능합니다.")
    @Schema(description = "레포지토리 타입 (server/client)", example = "client")
    private String type;

    @NotBlank(message = "프로젝트 설명은 필수입니다.")
    @Schema(description = "프로젝트 설명", example = "A lightweight MCP client written in TypeScript")
    private String description;

    @NotBlank(message = "GitHub 레포지토리 URL은 필수입니다.")
    @Schema(description = "GitHub 레포지토리 URL", example = "https://github.com/hongildong/my-mcp-client")
    private String repoUrl;

    @Schema(description = "웹사이트 URL (선택사항)", example = "https://my-mcp-client.example.com")
    private String websiteUrl;

    @Schema(description = "태그 목록", example = "[\"mcp\", \"client\", \"typescript\"]")
    private List<String> tags;

    @Email(message = "Please enter a valid email address")
    @Schema(description = "이메일", example = "gildong@example.com")
    private String email;

    /**
     * websiteUrl이 null일 경우 빈 문자열을 반환합니다.
     * @return websiteUrl 또는 빈 문자열
     */
    public String getWebsiteUrl() {
        return websiteUrl == null ? "" : websiteUrl;
    }

    /**
     * email이 null일 경우 빈 문자열을 반환합니다.
     * @return email 또는 빈 문자열
     */
    public String getEmail() {
        return email == null ? "" : email;
    }
}
