package com.miraclestudio.mcpreposerver.domain.repository.client;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import com.miraclestudio.mcpreposerver.dto.github.GitHubContributorResponse;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "ClientContributor")
@Table(name = "client_contributors")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ClientContributor extends BaseTimeEntity {

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
    private Set<ClientContributorMapping> clientMappings = new LinkedHashSet<>();

    public void addClientRepository(ClientContributorMapping mapping) {
        this.clientMappings.add(mapping);
        mapping.setContributor(this);
    }
    public static ClientContributor createContributor(GitHubContributorResponse contributor) {
        return ClientContributor.builder()
                .name(contributor.getLogin())
                .username(contributor.getLogin())
                .githubUrl(contributor.getHtmlUrl())
                .avatarUrl(contributor.getAvatarUrl())
                .build();
    }
}