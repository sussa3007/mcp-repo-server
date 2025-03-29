package com.miraclestudio.mcpreposerver.domain.usecase;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import com.miraclestudio.mcpreposerver.domain.common.Tag;
import com.miraclestudio.mcpreposerver.domain.repository.client.ClientRepository;
import com.miraclestudio.mcpreposerver.domain.repository.server.ServerRepository;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "use_cases")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UseCase extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long useCaseId;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 500)
    private String summary;

    private String imageUrl;

    @Column(nullable = false)
    private String author;

    private String authorAvatarUrl;

    private String industry;

    @OneToMany(mappedBy = "useCase", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UseCaseTag> useCaseTags = new LinkedHashSet<>();

    @OneToMany(mappedBy = "useCase", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UseCaseServerMapping> serverMappings = new LinkedList<>();

    @OneToMany(mappedBy = "useCase", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UseCaseClientMapping> clientMappings = new LinkedList<>();

    /**
     * 태그 추가
     */
    public void addUseCaseTag(UseCaseTag tag) {
        this.useCaseTags.add(tag);
        tag.setUseCase(this);
    }

    /**
     * 태그 제거
     */
    public void removeUseCaseTag(UseCaseTag tag) {
        this.useCaseTags.remove(tag);
        tag.setUseCase(null);
    }

    /**
     * 태그 목록 조회
     */
    public List<String> getTagNames() {
        return this.useCaseTags.stream()
                .map(UseCaseTag::getTag)
                .map(Tag::getName)
                .toList();
    }

    /**
     * 서버 레포지토리 추가
     */
    public void addServerRepository(ServerRepository serverRepository) {
        UseCaseServerMapping mapping = UseCaseServerMapping.builder()
                .useCase(this)
                .serverRepository(serverRepository)
                .build();
        this.serverMappings.add(mapping);
        mapping.setUseCase(this);
        mapping.setServerRepository(serverRepository);
    }

    /**
     * 클라이언트 레포지토리 추가
     */
    public void addClientRepository(ClientRepository clientRepository) {
        UseCaseClientMapping mapping = UseCaseClientMapping.builder()
                .useCase(this)
                .clientRepository(clientRepository)
                .build();
        this.clientMappings.add(mapping);
        mapping.setUseCase(this);
        mapping.setClientRepository(clientRepository);
    }

    /**
     * 사용 사례 내용 업데이트
     */
    public void updateContent(String title, String content, String summary, String imageUrl, String industry) {
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.imageUrl = imageUrl;
        this.industry = industry;
    }
}