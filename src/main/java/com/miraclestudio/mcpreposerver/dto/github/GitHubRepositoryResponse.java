package com.miraclestudio.mcpreposerver.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * GitHub API 저장소 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubRepositoryResponse {

    private Long id;
    private String name;
    private String description;
    private String url;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("stargazers_count")
    private Integer stars;

    @JsonProperty("forks_count")
    private Integer forks;

    private String language;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("pushed_at")
    private LocalDateTime pushedAt;

    private Owner owner;
    private License license;

    @JsonProperty("default_branch")
    private String defaultBranch;

    @JsonProperty("open_issues_count")
    private Integer openIssuesCount;

    @JsonProperty("topics")
    private String[] topics;

    private Boolean fork;
    private Boolean archived;
    private Boolean disabled;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Owner {
        private String login;
        @JsonProperty("avatar_url")
        private String avatarUrl;
        @JsonProperty("html_url")
        private String htmlUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class License {
        private String key;
        private String name;
        @JsonProperty("spdx_id")
        private String spdxId;
        private String url;

        // 간단한 생성자 추가
        public License(String name) {
            this.name = name;
        }
    }
}