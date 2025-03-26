package com.miraclestudio.mcpreposerver.domain.usecase;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import com.miraclestudio.mcpreposerver.domain.repository.client.ClientRepository;
import com.miraclestudio.mcpreposerver.domain.repository.server.ServerRepository;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "use_cases")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UseCase extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 10000)
    @Lob
    private String content;

    @Column(nullable = false, length = 500)
    private String summary;

    private String imageUrl;

    @Column(nullable = false)
    private String author;

    private String authorAvatarUrl;

    private String industry;

    @ElementCollection
    @CollectionTable(name = "use_case_tags", joinColumns = @JoinColumn(name = "use_case_id"))
    @Column(name = "tag")
    @Builder.Default
    private Set<String> tags = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "use_case_server_repositories", joinColumns = @JoinColumn(name = "use_case_id"), inverseJoinColumns = @JoinColumn(name = "server_repository_id"))
    @Builder.Default
    private List<ServerRepository> serverRepositories = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "use_case_client_repositories", joinColumns = @JoinColumn(name = "use_case_id"), inverseJoinColumns = @JoinColumn(name = "client_repository_id"))
    @Builder.Default
    private List<ClientRepository> clientRepositories = new ArrayList<>();

    /**
     * 태그 추가
     */
    public void addTag(String tag) {
        this.tags.add(tag);
    }

    /**
     * 태그 제거
     */
    public void removeTag(String tag) {
        this.tags.remove(tag);
    }

    /**
     * 서버 레포지토리 추가
     */
    public void addServerRepository(ServerRepository serverRepository) {
        if (!this.serverRepositories.contains(serverRepository)) {
            this.serverRepositories.add(serverRepository);
        }
    }

    /**
     * 서버 레포지토리 제거
     */
    public void removeServerRepository(ServerRepository serverRepository) {
        this.serverRepositories.remove(serverRepository);
    }

    /**
     * 클라이언트 레포지토리 추가
     */
    public void addClientRepository(ClientRepository clientRepository) {
        if (!this.clientRepositories.contains(clientRepository)) {
            this.clientRepositories.add(clientRepository);
        }
    }

    /**
     * 클라이언트 레포지토리 제거
     */
    public void removeClientRepository(ClientRepository clientRepository) {
        this.clientRepositories.remove(clientRepository);
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