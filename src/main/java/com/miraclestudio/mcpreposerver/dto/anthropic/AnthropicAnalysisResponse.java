package com.miraclestudio.mcpreposerver.dto.anthropic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Anthropic API 분석 결과 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnthropicAnalysisResponse {

    private String name;
    private String description;
    private List<String> tags;
    private Boolean isValidMcpRepo;
    private Integer score;
    private String analysis;
    private String recommendation;
    private String reason;
    private Boolean isOfficial;

    private List<McpTool> mcpTools;
    private List<EnvironmentVariable> environmentVariables;
    private List<Command> commands;
    private List<DeploymentOption> deploymentOptions;
    private SystemRequirements systemRequirements;
    private Database database;

    private List<String> supportedLanguages;
    private List<String> platforms;
    private List<String> usageExamples;
    private String installInstructions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class McpTool {
        private String name;
        private String description;
        private String usage;
        private String documentation;
        private Boolean isRequired;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnvironmentVariable {
        private String name;
        private String description;
        private Boolean required;
        private String defaultValue;
        private String type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Command {
        private String name;
        private String description;
        private String command;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeploymentOption {
        private String name;
        private String description;
        private String instructions;
        private String platform;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SystemRequirements {
        private String minimumCpu;
        private String recommendedCpu;
        private String minimumMemory;
        private String recommendedMemory;
        private String minimumStorage;
        private String recommendedStorage;
        private List<String> softwareRequirements;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Database {
        private String type;
        private String connectionString;
        private String description;
        private String descriptionSchema;
    }
}