package com.miraclestudio.mcpreposerver.domain.repository.client;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contributors")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Contributor extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String githubUsername;

    private String avatarUrl;

    private Integer contributions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_repository_id")
    private ClientRepository clientRepository;

    /**
     * 클라이언트 레포지토리 설정
     */
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
}