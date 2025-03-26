package com.miraclestudio.mcpreposerver.domain.repository.server;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "server_repositories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ServerRepository extends BaseTimeEntity {

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

    private String demoUrl;

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

    @OneToMany(mappedBy = "serverRepository", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ApiEndpoint> apiEndpoints = new ArrayList<>();

    @OneToMany(mappedBy = "serverRepository", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EnvironmentVariable> environmentVariables = new ArrayList<>();

    @Embedded
    private Database database;

    @Embedded
    private SystemRequirements systemRequirements;

    @OneToMany(mappedBy = "serverRepository", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Command> commands = new ArrayList<>();

    @OneToMany(mappedBy = "serverRepository", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DeploymentOption> deploymentOptions = new ArrayList<>();

    /**
     * API 엔드포인트 추가
     */
    public void addApiEndpoint(ApiEndpoint apiEndpoint) {
        apiEndpoints.add(apiEndpoint);
        apiEndpoint.setServerRepository(this);
    }

    /**
     * 환경 변수 추가
     */
    public void addEnvironmentVariable(EnvironmentVariable environmentVariable) {
        environmentVariables.add(environmentVariable);
        environmentVariable.setServerRepository(this);
    }

    /**
     * 명령어 추가
     */
    public void addCommand(Command command) {
        commands.add(command);
        command.setServerRepository(this);
    }

    /**
     * 배포 옵션 추가
     */
    public void addDeploymentOption(DeploymentOption deploymentOption) {
        deploymentOptions.add(deploymentOption);
        deploymentOption.setServerRepository(this);
    }

    /**
     * 데이터베이스 정보
     */
    @Embeddable
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Database {
        private String type;
        private String description;

        @Column(length = 2000)
        private String schema;
    }

    /**
     * 시스템 요구사항
     */
    @Embeddable
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class SystemRequirements {
        @Column(length = 500)
        private String hardware;

        @Column(length = 500)
        private String software;
    }
}