package com.miraclestudio.mcpreposerver.domain.submission;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import com.miraclestudio.mcpreposerver.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "submissions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Submission extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, unique = true)
    private Long repositoryId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RepositoryType type;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private String githubUrl;

    private String websiteUrl;

    @Column(nullable = false)
    private String tags;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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
     * 제출 상태 업데이트
     */
    public void updateStatus(Status status, String message) {
        this.status = status;
        this.message = message;
    }
}