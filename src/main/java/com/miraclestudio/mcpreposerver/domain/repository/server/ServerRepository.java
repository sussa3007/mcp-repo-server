package com.miraclestudio.mcpreposerver.domain.repository.server;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import com.miraclestudio.mcpreposerver.domain.usecase.UseCaseServerMapping;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "server_repositories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@DynamicUpdate
public class ServerRepository extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serverRepositoryId;

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


    @Column(length = 255)
    private String demoUrl;

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

    @OneToMany(mappedBy = "serverRepository", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ServerRepositoryTag> serverRepositoryTags = new LinkedHashSet<>();

    @Embedded
    private Database database;

    @Embedded
    private SystemRequirements systemRequirements;

    @OneToMany(mappedBy = "serverRepository", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<McpTool> mcpTools = new LinkedHashSet<>();

    @OneToMany(mappedBy = "serverRepository", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<EnvironmentVariable> environmentVariables = new LinkedHashSet<>();

    @OneToMany(mappedBy = "serverRepository", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Command> commands = new LinkedHashSet<>();

    @OneToMany(mappedBy = "serverRepository", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<DeploymentOption> deploymentOptions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "serverRepository", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ServerContributorMapping> contributorMappings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "serverRepository", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UseCaseServerMapping> useCaseMappings = new LinkedHashSet<>();

    public void addMcpTool(McpTool mcpTool) {
        this.mcpTools.add(mcpTool);
        mcpTool.setServerRepository(this);
    }

    public void addEnvironmentVariable(EnvironmentVariable environmentVariable) {
        this.environmentVariables.add(environmentVariable);
        environmentVariable.setServerRepository(this);
    }

    public void addCommand(Command command) {
        this.commands.add(command);
        command.setServerRepository(this);
    }

    public void addDeploymentOption(DeploymentOption deploymentOption) {
        this.deploymentOptions.add(deploymentOption);
        deploymentOption.setServerRepository(this);
    }

    public void addContributor(ServerContributorMapping mapping) {
        this.contributorMappings.add(mapping);
        mapping.setServerRepository(this);
    }

    public void setSystemRequirements(SystemRequirements systemRequirements) {
        this.systemRequirements = systemRequirements;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    /**
     * 태그 추가
     */
    public void addServerRepositoryTag(ServerRepositoryTag tag) {
        this.serverRepositoryTags.add(tag);
        tag.setServerRepository(this);
    }

    /**
     * 태그 제거
     */
    public void removeServerRepositoryTag(ServerRepositoryTag tag) {
        this.serverRepositoryTags.remove(tag);
        tag.setServerRepository(null);
    }

    /**
     * 태그 목록 조회
     */
    public List<String> getTagNames() {
        return this.serverRepositoryTags.stream()
                .map(tag -> tag.getTag().getName())
                .toList();
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

        @Column(name = "database_description")
        private String description;

        @Column(length = 2000)
        private String descriptionSchema;
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