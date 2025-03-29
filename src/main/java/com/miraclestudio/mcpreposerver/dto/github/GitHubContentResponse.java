package com.miraclestudio.mcpreposerver.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubContentResponse {

    private String name;
    private String path;
    private String sha;
    private Long size;
    private String url;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("git_url")
    private String gitUrl;

    @JsonProperty("download_url")
    private String downloadUrl;

    private String type;
    private String content;
    private String encoding;

    /**
     * 디렉토리인지 여부
     * 
     * @return 디렉토리인 경우 true, 파일인 경우 false
     */
    public boolean isDirectory() {
        return "dir".equals(type);
    }

    /**
     * 파일인지 여부
     * 
     * @return 파일인 경우 true, 디렉토리인 경우 false
     */
    public boolean isFile() {
        return "file".equals(type);
    }

    /**
     * Base64로 인코딩된 내용을 디코딩하여 반환
     * 
     * @return 디코딩된 내용
     */
    public String getDecodedContent() {
        if (content == null || !encoding.equalsIgnoreCase("base64")) {
            return content;
        }
        return new String(java.util.Base64.getDecoder().decode(content));
    }
}