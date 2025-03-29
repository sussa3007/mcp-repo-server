package com.miraclestudio.mcpreposerver.dto.response.repository;

import com.miraclestudio.mcpreposerver.domain.repository.server.ServerRepository;
import com.miraclestudio.mcpreposerver.domain.repository.server.Command;
import com.miraclestudio.mcpreposerver.domain.repository.server.Contributor;
import com.miraclestudio.mcpreposerver.domain.repository.server.ServerRepository.Database;
import com.miraclestudio.mcpreposerver.domain.repository.server.DeploymentOption;
import com.miraclestudio.mcpreposerver.domain.repository.server.EnvironmentVariable;
import com.miraclestudio.mcpreposerver.domain.repository.server.McpTool;
import com.miraclestudio.mcpreposerver.domain.repository.server.ServerRepository.SystemRequirements;
import com.miraclestudio.mcpreposerver.domain.repository.server.ServerRepositoryTag;
import com.miraclestudio.mcpreposerver.domain.repository.server.ServerContributorMapping;
import com.miraclestudio.mcpreposerver.domain.common.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for representing ServerRepository in API responses
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerRepositoryResponseDto {
    private Long id;
    private String name;
    private String description;
    private String owner;
    private String repo;
    private String githubUrl;
    private String demoUrl;
    private Boolean isOfficial;
    private String language;
    private Integer stars;
    private Integer forks;
    private String license;
    private List<String> tags;
    private DatabaseDto database;
    private SystemRequirementsDto systemRequirements;
    private List<McpToolDto> mcpTools;
    private List<EnvironmentVariableDto> environmentVariables;
    private List<CommandDto> commands;
    private List<DeploymentOptionDto> deploymentOptions;
    private List<ContributorDto> contributors;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Convert ServerRepository entity to ServerRepositoryResponseDto
     *
     * @param entity ServerRepository entity
     * @return ServerRepositoryResponseDto
     */
    public static ServerRepositoryResponseDto from(ServerRepository entity) {
        if (entity == null) {
            return null;
        }

        return ServerRepositoryResponseDto.builder()
                .id(entity.getServerRepositoryId())
                .name(entity.getName())
                .description(entity.getDescription())
                .owner(entity.getOwner())
                .repo(entity.getRepo())
                .githubUrl(entity.getGithubUrl())
                .demoUrl(entity.getDemoUrl())
                .isOfficial(entity.getIsOfficial())
                .language(entity.getLanguage())
                .stars(entity.getStars())
                .forks(entity.getForks())
                .license(entity.getLicense())
                .tags(entity.getServerRepositoryTags().stream()
                        .map(ServerRepositoryTag::getTag)
                        .map(Tag::getName)
                        .collect(Collectors.toList()))
                .database(entity.getDatabase() != null ? DatabaseDto.from(entity.getDatabase()) : null)
                .systemRequirements(entity.getSystemRequirements() != null ? SystemRequirementsDto.from(entity.getSystemRequirements()) : null)
                .mcpTools(entity.getMcpTools().stream()
                        .map(McpToolDto::from)
                        .collect(Collectors.toList()))
                .environmentVariables(entity.getEnvironmentVariables().stream()
                        .map(EnvironmentVariableDto::from)
                        .collect(Collectors.toList()))
                .commands(entity.getCommands().stream()
                        .map(CommandDto::from)
                        .collect(Collectors.toList()))
                .deploymentOptions(entity.getDeploymentOptions().stream()
                        .map(DeploymentOptionDto::from)
                        .collect(Collectors.toList()))
                .contributors(entity.getContributorMappings().stream()
                        .map(ServerContributorMapping::getContributor)
                        .map(ContributorDto::from)
                        .collect(Collectors.toList()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * DTO for database information
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DatabaseDto {
        private String type;
        private String description;
        private String descriptionSchema;

        public static DatabaseDto from(Database entity) {
            if (entity == null) {
                return null;
            }

            return DatabaseDto.builder()
                    .type(entity.getType())
                    .description(entity.getDescription())
                    .descriptionSchema(entity.getDescriptionSchema())
                    .build();
        }
    }

    /**
     * DTO for system requirements information
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SystemRequirementsDto {
        private String hardware;
        private String software;

        public static SystemRequirementsDto from(SystemRequirements entity) {
            if (entity == null) {
                return null;
            }

            return SystemRequirementsDto.builder()
                    .hardware(entity.getHardware())
                    .software(entity.getSoftware())
                    .build();
        }
    }

    /**
     * DTO for MCP tool information
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class McpToolDto {
        private String name;
        private String description;
        private String version;
        private String usageInfo;
        private String documentation;
        private Boolean isRequired;

        public static McpToolDto from(McpTool entity) {
            if (entity == null) {
                return null;
            }

            return McpToolDto.builder()
                    .name(entity.getName())
                    .description(entity.getDescription())
                    .version(entity.getVersion())
                    .usageInfo(entity.getUsageInfo())
                    .documentation(entity.getDocumentation())
                    .isRequired(entity.getIsRequired())
                    .build();
        }
    }

    /**
     * DTO for environment variable information
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnvironmentVariableDto {
        private String name;
        private String description;
        private Boolean required;
        private String defaultValue;
        private String type;

        public static EnvironmentVariableDto from(EnvironmentVariable entity) {
            if (entity == null) {
                return null;
            }

            return EnvironmentVariableDto.builder()
                    .name(entity.getName())
                    .description(entity.getDescription())
                    .required(entity.getRequired())
                    .defaultValue(entity.getDefaultValue())
                    .type(entity.getType())
                    .build();
        }
    }

    /**
     * DTO for command information
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommandDto {
        private String name;
        private String command;
        private String description;
        private String type;
        private String platform;

        public static CommandDto from(Command entity) {
            if (entity == null) {
                return null;
            }

            return CommandDto.builder()
                    .name(entity.getName())
                    .command(entity.getCommand())
                    .description(entity.getDescription())
                    .type(entity.getType())
                    .platform(entity.getPlatform())
                    .build();
        }
    }

    /**
     * DTO for deployment option information
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeploymentOptionDto {
        private String name;
        private String description;

        public static DeploymentOptionDto from(DeploymentOption entity) {
            if (entity == null) {
                return null;
            }

            return DeploymentOptionDto.builder()
                    .name(entity.getName())
                    .description(entity.getDescription())
                    .build();
        }
    }

    /**
     * DTO for contributor information
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContributorDto {
        private String name;
        private String githubUrl;
        private String avatarUrl;

        public static ContributorDto from(Contributor entity) {
            if (entity == null) {
                return null;
            }

            return ContributorDto.builder()
                    .name(entity.getName())
                    .githubUrl(entity.getGithubUrl())
                    .avatarUrl(entity.getAvatarUrl())
                    .build();
        }
    }
}

