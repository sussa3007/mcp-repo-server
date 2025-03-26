package com.miraclestudio.mcpreposerver.domain.repository.client;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client_repositories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ClientRepository extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false)
    private String repo;

    @Column(nullable = false)
    private String githubUrl;

    @Column(nullable = false)
    private Boolean isOfficial;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private Integer stars;

    @Column(nullable = false)
    private Integer forks;

    @Column(nullable = false)
    private String license;

    @Column(nullable = false)
    private String tags;

    @Column(nullable = false)
    private String installInstructions;

    @ElementCollection
    @CollectionTable(name = "client_repository_usage_examples", joinColumns = @JoinColumn(name = "client_repository_id"))
    @Column(name = "example", length = 2000)
    @Builder.Default
    private List<String> usageExamples = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "client_repository_supported_languages", joinColumns = @JoinColumn(name = "client_repository_id"))
    @Column(name = "language")
    @Builder.Default
    private List<String> supportedLanguages = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "client_repository_platforms", joinColumns = @JoinColumn(name = "client_repository_id"))
    @Column(name = "platform")
    @Builder.Default
    private List<String> platforms = new ArrayList<>();

    @OneToMany(mappedBy = "clientRepository", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Contributor> contributors = new ArrayList<>();

    /**
     * 기여자 추가
     */
    public void addContributor(Contributor contributor) {
        contributors.add(contributor);
        contributor.setClientRepository(this);
    }
}