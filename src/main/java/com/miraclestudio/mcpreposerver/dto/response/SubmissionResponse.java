package com.miraclestudio.mcpreposerver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import com.miraclestudio.mcpreposerver.domain.submission.Submission;
import com.miraclestudio.mcpreposerver.domain.submission.SubmissionTag;
import com.miraclestudio.mcpreposerver.domain.common.Tag;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionResponse {

    private Long submissionId;
    private String name;
    private String author;
    private Long repositoryId;
    private String type;
    private String description;
    private String repoUrl;
    private String websiteUrl;
    private String email;
    private String status;
    private String message;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SubmissionResponse from(Submission submission) {
        return SubmissionResponse.builder()
                .submissionId(submission.getSubmissionId())
                .name(submission.getName())
                .author(submission.getAuthor())
                .repositoryId(submission.getRepositoryId())
                .type(submission.getType().name())
                .description(submission.getDescription())
                .repoUrl(submission.getRepoUrl())
                .websiteUrl(submission.getWebsiteUrl())
                .email(submission.getEmail())
                .status(submission.getStatus().name())
                .message(submission.getMessage())
                .tags(submission.getSubmissionTags().stream()
                        .map(SubmissionTag::getTag)
                        .map(Tag::getName)
                        .collect(Collectors.toList()))
                .createdAt(submission.getCreatedAt())
                .updatedAt(submission.getUpdatedAt())
                .build();
    }
}