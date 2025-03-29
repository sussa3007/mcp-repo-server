package com.miraclestudio.mcpreposerver.domain.repository.server;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import com.miraclestudio.mcpreposerver.dto.github.GitHubContributorResponse;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "ServerContributor")
@Table(name = "server_contributors")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Contributor extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contributorId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100, unique = true)
    private String username;

    @Column(length = 255)
    private String avatarUrl;

    @Column(nullable = false, unique = true)
    private String githubUrl;


    @OneToMany(mappedBy = "contributor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ServerContributorMapping> serverMappings = new LinkedHashSet<>();

    public void addServerRepository(ServerContributorMapping mapping) {
        this.serverMappings.add(mapping);
        mapping.setContributor(this);
    }

    public static Contributor createContributor(GitHubContributorResponse contributor) {
        return Contributor.builder()
                .name(contributor.getLogin())
                .username(contributor.getLogin())
                .avatarUrl(contributor.getAvatarUrl())
                .githubUrl(contributor.getHtmlUrl())
                .build();
    }
}