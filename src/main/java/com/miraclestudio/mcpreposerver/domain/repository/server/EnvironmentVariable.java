package com.miraclestudio.mcpreposerver.domain.repository.server;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "environment_variables")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EnvironmentVariable extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long environmentVariableId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String defaultValue;

    @Column(nullable = false)
    private Boolean required;

    @Column(length = 50)
    private String type;

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