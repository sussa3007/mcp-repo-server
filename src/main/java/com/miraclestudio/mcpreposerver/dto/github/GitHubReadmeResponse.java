package com.miraclestudio.mcpreposerver.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

/**
 * GitHub API README 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubReadmeResponse {

    private String name;
    private String path;
    private String sha;
    private long size;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("download_url")
    private String downloadUrl;

    @JsonProperty("git_url")
    private String gitUrl;

    private String type;
    private String content;
    private String encoding;

    /**
     * Base64로 인코딩된 내용을 디코딩해서 반환
     */
    public String getDecodedContent() {
        if (content == null || !encoding.equals("base64")) {
            return "";
        }

        try {
            // 깃허브 API는 개행을 포함한 base64 인코딩을 사용하므로 먼저 개행 문자 제거
            String sanitizedContent = content.replace("\n", "");
            byte[] decodedBytes = Base64.getDecoder().decode(sanitizedContent);
            return new String(decodedBytes);
        } catch (IllegalArgumentException e) {
            return "";
        }
    }
}