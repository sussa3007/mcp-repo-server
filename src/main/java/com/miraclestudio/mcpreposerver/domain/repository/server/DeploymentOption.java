package com.miraclestudio.mcpreposerver.domain.repository.server;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "deployment_options")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DeploymentOption extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deploymentOptionId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(length = 50)
    private String platform;

    @Column(length = 100)
    private String dockerImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_repository_id")
    private ServerRepository serverRepository;

    /**
     * 서버 레포지토리 설정
     */
    public void setServerRepository(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }
}