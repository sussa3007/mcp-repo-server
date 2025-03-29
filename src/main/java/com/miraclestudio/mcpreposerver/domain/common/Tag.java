package com.miraclestudio.mcpreposerver.domain.common;

import com.miraclestudio.mcpreposerver.domain.post.PostTag;
import com.miraclestudio.mcpreposerver.domain.submission.SubmissionTag;
import com.miraclestudio.mcpreposerver.domain.usecase.UseCaseTag;
import com.miraclestudio.mcpreposerver.domain.repository.server.ServerRepositoryTag;
import com.miraclestudio.mcpreposerver.domain.repository.client.ClientRepositoryTag;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "tag", orphanRemoval = true)
    @Builder.Default
    private Set<PostTag> postTags = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tag", orphanRemoval = true)
    @Builder.Default
    private Set<SubmissionTag> submissionTags = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tag", orphanRemoval = true)
    @Builder.Default
    private Set<UseCaseTag> useCaseTags = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tag", orphanRemoval = true)
    @Builder.Default
    private Set<ServerRepositoryTag> serverRepositoryTags = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tag", orphanRemoval = true)
    @Builder.Default
    private Set<ClientRepositoryTag> clientRepositoryTags = new LinkedHashSet<>();

    // 태그의 이름 정규화를 위한 헬퍼 메서드
    public static String normalizeTagName(String name) {
        return name.toLowerCase().trim().replaceAll("\\s+", "-");
    }

    /**
     * 빌더 패턴에서 name 필드를 위한 커스텀 빌더 메서드
     */
    public static class TagBuilder {
        public TagBuilder name(String name) {
            this.name = normalizeTagName(name);
            return this;
        }
    }

    /**
     * Post 태그 매핑 추가
     */
    public void addPostTag(PostTag postTag) {
        this.postTags.add(postTag);
    }

    /**
     * Submission 태그 매핑 추가
     */
    public void addSubmissionTag(SubmissionTag submissionTag) {
        this.submissionTags.add(submissionTag);
    }

    /**
     * UseCase 태그 매핑 추가
     */
    public void addUseCaseTag(UseCaseTag useCaseTag) {
        this.useCaseTags.add(useCaseTag);
    }

    /**
     * ServerRepository 태그 매핑 추가
     */
    public void addServerRepositoryTag(ServerRepositoryTag serverRepositoryTag) {
        this.serverRepositoryTags.add(serverRepositoryTag);
    }

    /**
     * ClientRepository 태그 매핑 추가
     */
    public void addClientRepositoryTag(ClientRepositoryTag clientRepositoryTag) {
        this.clientRepositoryTags.add(clientRepositoryTag);
    }
}