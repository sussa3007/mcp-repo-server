package com.miraclestudio.mcpreposerver.domain.submission;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import com.miraclestudio.mcpreposerver.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "submissions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Submission extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    @Setter
    private Long repositoryId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RepositoryType type;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private String repoUrl;

    private String websiteUrl;

    // 태그와의 관계 설정
    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<SubmissionTag> submissionTags = new LinkedHashSet<>();

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(columnDefinition = "TEXT")
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 레포지토리 유형
     */
    public enum RepositoryType {
        SERVER, CLIENT
    }

    /**
     * 제출 상태
     */
    public enum Status {
        PENDING, APPROVED, REJECTED
    }

    /**
     * 태그 추가
     */
    public void addSubmissionTag(SubmissionTag tag) {
        this.submissionTags.add(tag);
        tag.setSubmission(this);
    }

    /**
     * 태그 제거
     */
    public void removeSubmissionTag(SubmissionTag tag) {
        this.submissionTags.remove(tag);
        tag.setSubmission(null);
    }

    /**
     * 태그 목록 조회
     */
    public List<String> getTagNames() {
        return this.submissionTags.stream()
                .map(tag -> tag.getTag().getName())
                .toList();
    }

    /**
     * 제출 상태 업데이트
     */
    public void updateStatus(Status status, String message) {
        this.status = status;
        this.message = message;
    }
}