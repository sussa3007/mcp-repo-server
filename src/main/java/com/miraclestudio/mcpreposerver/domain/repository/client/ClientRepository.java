package com.miraclestudio.mcpreposerver.domain.repository.client;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import com.miraclestudio.mcpreposerver.domain.usecase.UseCaseClientMapping;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "client_repositories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@DynamicUpdate
public class ClientRepository extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientRepositoryId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 100)
    private String owner;

    @Column(nullable = false, length = 100)
    private String repo;

    @Column(nullable = false, unique = true)
    private String githubUrl;

    @Column(nullable = false)
    private Boolean isOfficial;

    @Column(length = 50)
    private String language;

    @Column
    private Integer stars;

    @Column
    private Integer forks;

    @Column(length = 100)
    private String license;

    @OneToMany(mappedBy = "clientRepository", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ClientRepositoryTag> clientRepositoryTags = new LinkedHashSet<>();

    @Column(columnDefinition = "TEXT")
    private String installInstructions;

    @ElementCollection
    @CollectionTable(name = "client_repository_usage_examples", joinColumns = @JoinColumn(name = "client_repository_id"))
    @Column(name = "usage_example", columnDefinition = "TEXT")
    @Builder.Default
    private Set<String> usageExamples = new LinkedHashSet<>();

    @ElementCollection
    @CollectionTable(name = "client_repository_supported_languages", joinColumns = @JoinColumn(name = "client_repository_id"))
    @Column(name = "supported_language")
    @Builder.Default
    private Set<String> supportedLanguages = new LinkedHashSet<>();

    @ElementCollection
    @CollectionTable(name = "client_repository_platforms", joinColumns = @JoinColumn(name = "client_repository_id"))
    @Column(name = "platform")
    @Builder.Default
    private Set<String> platforms = new LinkedHashSet<>();

    @OneToMany(mappedBy = "clientRepository", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ClientContributorMapping> contributorMappings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "clientRepository", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UseCaseClientMapping> useCaseMappings = new LinkedHashSet<>();

    public void addContributor(ClientContributorMapping mapping) {
        this.contributorMappings.add(mapping);
        mapping.setClientRepository(this);
    }

    /**
     * 태그 추가
     */
    public void addClientRepositoryTag(ClientRepositoryTag tag) {
        this.clientRepositoryTags.add(tag);
        tag.setClientRepository(this);
    }

    /**
     * 태그 제거
     */
    public void removeClientRepositoryTag(ClientRepositoryTag tag) {
        this.clientRepositoryTags.remove(tag);
        tag.setClientRepository(null);
    }

    /**
     * 태그 목록 조회
     */
    public List<String> getTagNames() {
        return this.clientRepositoryTags.stream()
                .map(tag -> tag.getTag().getName())
                .toList();
    }

    /**
     * 지원 언어 추가
     */
    public void addSupportedLanguage(String language) {
        this.supportedLanguages.add(language);
    }

    /**
     * 지원 플랫폼 추가
     */
    public void addPlatform(String platform) {
        this.platforms.add(platform);
    }

    /**
     * 사용 예제 추가
     */
    public void addUsageExample(String example) {
        this.usageExamples.add(example);
    }
}