package com.miraclestudio.mcpreposerver.service.anthropic;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.ContentBlock;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.google.gson.Gson;
import com.miraclestudio.mcpreposerver.dto.github.GitHubReadmeResponse;
import com.miraclestudio.mcpreposerver.dto.github.GitHubRepositoryResponse;
import com.miraclestudio.mcpreposerver.dto.anthropic.AnthropicAnalysisResponse;
import com.miraclestudio.mcpreposerver.exception.BusinessException;
import com.miraclestudio.mcpreposerver.exception.ErrorCode;
import com.miraclestudio.mcpreposerver.service.github.GitHubService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import jakarta.annotation.PostConstruct;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnthropicService {

    private final GitHubService githubService;
    private final Gson gson = new Gson();
    private AnthropicClient anthropicClient;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Value("${anthropic.api.key}")
    private String apiKey;

    @Value("${anthropic.api.model:claude-3-opus-20240229}")
    private String apiModel;

    @Value("${anthropic.api.max-tokens:4096}")
    private int maxTokens;

    @PostConstruct
    public void init() {
        log.info("AnthropicClient 초기화 시작");
        try {
            this.anthropicClient = AnthropicOkHttpClient.builder()
                    .apiKey(apiKey)
                    .timeout(java.time.Duration.ofSeconds(120))
                    .build();
            log.info("AnthropicClient 초기화 완료");
        } catch (Exception e) {
            log.error("AnthropicClient 초기화 실패: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Anthropic 클라이언트 초기화 실패");
        }
    }

    public AnthropicAnalysisResponse analyzeRepository(GitHubRepositoryResponse repoResponse, String readmeContent,
            String repoType) {
        log.info("레포지토리 분석 시작 - 레포지토리: {}", repoResponse.getName());
        try {
            String owner = repoResponse.getOwner().getLogin();
            String repo = repoResponse.getName();

            readmeContent = validateReadmeContent(readmeContent);

            RepositoryAnalysisResult result = analyzeRepositoryInternal(owner, repo, repoType, repoResponse,
                    readmeContent);
            return convertToAnalysisResponse(result, repoResponse, repoType);
        } catch (Exception e) {
            log.error("레포지토리 분석 중 오류 발생: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "레포지토리 분석 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private String validateReadmeContent(String readmeContent) {
        if (readmeContent == null || readmeContent.trim().isEmpty()) {
            log.debug("README 내용 없음, 기본값 사용");
            return "README 내용이 없습니다.";
        }
        return readmeContent;
    }

    private RepositoryAnalysisResult analyzeRepositoryInternal(String owner, String repo, String repoType,
            GitHubRepositoryResponse repoResponse, String readmeContent) {
        log.debug("레포지토리 분석 프로세스 시작");
        String prompt = buildPrompt(repoResponse, readmeContent, repoType);
        String claudeResponse = callAnthropicApi(prompt);
        return parseClaudeResponse(claudeResponse);
    }

    private String buildPrompt(GitHubRepositoryResponse repo, String readme, String repoType) {
        log.debug("프롬프트 구성 시작 - 레포지토리: {}, 타입: {}", repo.getName(), repoType);

        StringBuilder prompt = new StringBuilder()
                .append("# 레포지토리 분석 요청\n\n")
                .append("다음 GitHub 레포지토리에 대한 분석을 수행해 주세요.\n\n")
                .append("## 레포지토리 정보\n")
                .append(String.format("- 이름: %s\n", repo.getName()))
                .append(String.format("- 설명: %s\n", repo.getDescription() != null ? repo.getDescription() : ""))
                .append(String.format("- 소유자: %s\n", repo.getOwner().getLogin()))
                .append(String.format("- 언어: %s\n", repo.getLanguage()))
                .append(String.format("- 별점 수: %d\n", repo.getStars()))
                .append(String.format("- 포크 수: %d\n", repo.getForks()))
                .append(String.format("- 라이센스: %s\n\n",
                        repo.getLicense() != null ? repo.getLicense().getName() : "명시되지 않음"))
                .append("## README 내용\n")
                .append(readme != null ? readme : "README 내용 없음")
                .append("\n\n## 분석 요청\n")
                .append(String.format("이 레포지토리가 MCP(Model Context Protocol) %s로서 적합한지 평가해 주세요.\n", repoType))
                .append(String.format(
                        "이 레포지토리가 기업이 구현하였고, 클라이언트 레포지토리라면, MCP 클라이언트를 범용적으로 사용할수 있게 도와주는 프로젝트로서 다소 MCP 클라이언트 라이브러리의 범위를 벗어날수 있습니다.\n"))
                .append(String.format(
                        "하지만, 코드 편집기, AI 프롬프트 엔진 등 클라이언트 툴로서 MCP 서버를 연동하여 사용할수 있다고 판단되면 클라이언트 레포지토리로 평가해 주세요.\n"))
                .append(String.format(
                        "기업이 아닌 일반 개인이 개발한 클라이언트 레포지토리는 README 내용과 전반적인 내용을 모두 종합하여 기업의 클라이언트 레포지토리랑 다르게 평가해주세요.\n"))
                .append(String.format("이 레포지토리의 태그를 분석해 지정해 주세요 8개 이하로 지정해 주세요.\n"))
                .append(String.format("태그는 절대 중복 되지 않도록 지정해 주세요.\n"))
                .append(String.format("Score는 0~100점 사이로 평가해 주세요. 평가 기준은 다음과 같습니다.\n"))
                .append("1. 개발 조직의 신뢰성 (20점)\n")
                .append("   - 개발 조직이 AI/ML 관련 기업인가?\n")
                .append("   - 개발 조직의 기술적 전문성과 평판\n")
                .append("   - 오픈소스 커뮤니티에서의 활동도\n")
                .append("2. 기술적 완성도 (20점)\n")
                .append("   - MCP 프로토콜의 정확한 구현\n")
                .append("   - 코드 품질 및 아키텍처\n")
                .append("   - 테스트 커버리지 및 품질\n")
                .append("3. 유지보수 및 지원 (20점)\n")
                .append("   - 정기적인 업데이트 빈도\n")
                .append("   - 이슈 응답 시간 및 해결률\n")
                .append("   - 문서화 품질 및 완성도\n")
                .append("4. 커뮤니티 활성도 (20점)\n")
                .append("   - GitHub 스타 및 포크 수\n")
                .append("   - 컨트리뷰터 수 및 활동성\n")
                .append("   - 커뮤니티 참여도 (이슈, PR 등)\n")
                .append("5. 보안 및 안정성 (20점)\n")
                .append("   - 보안 취약점 대응 체계\n")
                .append("   - 의존성 관리 및 업데이트\n")
                .append("   - 라이센스 준수 및 명확성\n")
                .append("\n각 항목은 세부 기준에 따라 평가되며, 총점 80점 이상인 경우 Official 레포지토리로 인정됩니다.\n")
                .append("\n기업이 아닌 일반 개인이 개발한 레포지토리는 80점을 넘기지 않도록 평가해 주세요.\n")
                .append("특히 AI/ML 관련 기업이 개발한 레포지토리의 경우, 개발 조직의 신뢰성 항목에서 높은 점수를 받을 수 있습니다.\n\n")
                .append("다음 항목에 대해 분석해 주세요:\n\n");

        if ("SERVER".equals(repoType)) {
            appendServerAnalysisItems(prompt);
        } else {
            appendClientAnalysisItems(prompt);
        }

        appendResponseFormat(prompt, repoType);

        log.debug("프롬프트 구성 완료, 길이: {}", prompt.length());
        return prompt.toString();
    }

    private void appendServerAnalysisItems(StringBuilder prompt) {
        prompt.append("1. MCP 서버 프로토콜 구현 여부\n")
                .append("2. MCP 서버 Tool 사용 여부 및 목록\n")
                .append("3. 환경 변수 및 설정 옵션\n")
                .append("4. 배포 및 설치 방법\n")
                .append("5. 시스템 요구사항\n");
    }

    private void appendClientAnalysisItems(StringBuilder prompt) {
        prompt.append("1. MCP 클라이언트 라이브러리 구현 여부\n")
                .append("2. 지원 언어 및 플랫폼\n")
                .append("3. 사용 예제 및 문서화 수준\n")
                .append("4. 설치 방법\n");
    }

    private void appendResponseFormat(StringBuilder prompt, String repoType) {
        prompt.append("\n## 응답 형식\n")
                .append("{!중요한 부분} 영어로 결과를 JSON 형식으로 반환해 주세요.\n")
                .append("{\n")
                .append("  \"tags\": [\"tag1\", \"tag2\", ...],\n")
                .append("  \"isValidMcpRepo\": true/false,\n")
                .append("  \"score\": 0-100,\n")
                .append("  \"analysis\": \"분석 내용...\",\n")
                .append("  \"recommendation\": \"Approve\" 또는 \"Reject\",\n")
                .append("  \"reason\": \"승인/거부 이유\",\n");

        if ("SERVER".equals(repoType)) {
            prompt.append(
                    "  \"tools\": [{\"name\": \"\", \"description\": \"\", \"inputs\": \"\", \"outputs\": \"\"}],\n")
                    .append("  \"environmentVariables\": [],\n")
                    .append("  \"deploymentOptions\": [],\n");
        } else {
            prompt.append("  \"supportedLanguages\": [],\n")
                    .append("  \"platforms\": [],\n")
                    .append("  \"usageExamples\": []\n");
        }
        prompt.append("}\n");
    }

    private String callAnthropicApi(String prompt) {
        log.info("Anthropic API 호출 시작 - 프롬프트 길이: {}", prompt.length());
        try {
            MessageCreateParams params = MessageCreateParams.builder()
                    .model(Model.CLAUDE_3_7_SONNET_LATEST)
                    .maxTokens(maxTokens)
                    .temperature(0.1)
                    .system("당신은 MCP 레포지토리 분석 전문가입니다. GitHub 레포지토리를 분석하고 JSON 형식으로 결과를 반환해주세요.")
                    .addUserMessage(prompt)
                    .build();

            Message response = anthropicClient.messages().create(params);
            return extractResponseText(response);
        } catch (Exception e) {
            log.error("Anthropic API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR,
                    "Anthropic API 호출 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private String extractResponseText(Message response) {
        if (response.content() == null || response.content().isEmpty()) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Anthropic API 응답에 콘텐츠가 없습니다.");
        }

        StringBuilder responseText = new StringBuilder();
        for (ContentBlock content : response.content()) {
            responseText.append(content.text());
        }

        if (responseText.length() == 0) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Anthropic API 응답에서 텍스트를 추출할 수 없습니다.");
        }

        return responseText.toString();
    }

    private RepositoryAnalysisResult parseClaudeResponse(String response) {
        try {
            log.debug("Claude 응답 파싱 시작: {}", response);

            // TextBlock 형식 처리
            String jsonContent;
            if (response.contains("TextBlock{")) {
                // TextBlock 형식에서 text 필드 추출
                int textStart = response.indexOf("text=") + 5;
                int textEnd = response.lastIndexOf(", type=");
                if (textEnd == -1) {
                    textEnd = response.lastIndexOf("}");
                }
                jsonContent = response.substring(textStart, textEnd);
            } else {
                jsonContent = response;
            }

            // JSON 마커 처리
            if (jsonContent.contains("```json")) {
                int start = jsonContent.indexOf("```json") + 7;
                int end = jsonContent.lastIndexOf("```");
                if (end > start) {
                    jsonContent = jsonContent.substring(start, end).trim();
                }
            }

            try {
                return objectMapper.readValue(jsonContent, RepositoryAnalysisResult.class);
            } catch (JsonProcessingException e) {
                log.error("JSON 파싱 실패: {}", e.getMessage());
                log.error("파싱 시도한 JSON: {}", jsonContent);
                throw e;
            }
        } catch (Exception e) {
            log.error("Claude 응답 파싱 중 오류 발생: {}", e.getMessage());
            log.error("파싱 실패한 응답: {}", response);
            throw new BusinessException(ErrorCode.AI_SERVICE_ERROR, "분석 결과를 파싱할 수 없습니다.");
        }
    }

    private AnthropicAnalysisResponse convertToAnalysisResponse(RepositoryAnalysisResult result,
            GitHubRepositoryResponse repoResponse, String repoType) {
        log.debug("분석 결과 변환 시작 - 레포지토리: {}", repoResponse.getName());
        AnthropicAnalysisResponse response = new AnthropicAnalysisResponse();

        // 공통 필드 설정
        response.setName(repoResponse.getName());
        response.setDescription(repoResponse.getDescription() != null ? repoResponse.getDescription() : "");
        response.setTags(Arrays.asList(result.tags));
        response.setIsValidMcpRepo(result.isValidMcpRepo);
        response.setScore(result.score);
        response.setAnalysis(result.analysis);
        response.setRecommendation(result.recommendation);
        response.setReason(result.reason);
        response.setIsOfficial(result.score >= 80);

        // 서버 타입인 경우
        if ("SERVER".equalsIgnoreCase(repoType)) {
            setMcpTools(response, result.tools);
            setEnvironmentVariables(response, result.environmentVariables);
            setCommands(response);
            setDeploymentOptions(response, result.deploymentOptions);
            setSystemRequirements(response);
            setDatabase(response);
        }
        // 클라이언트 타입인 경우
        else if ("CLIENT".equalsIgnoreCase(repoType)) {
            response.setSupportedLanguages(Arrays.asList(result.supportedLanguages));
            response.setPlatforms(Arrays.asList(result.platforms));
            response.setUsageExamples(Arrays.asList(result.usageExamples));
            response.setInstallInstructions("npm install " + repoResponse.getName()); // 기본값
        }

        log.debug("분석 결과 변환 완료");
        return response;
    }

    private void setMcpTools(AnthropicAnalysisResponse response, Tools[] tools) {
        List<AnthropicAnalysisResponse.McpTool> mcpTools = new ArrayList<>();
        if (tools != null) {
            for (Tools tool : tools) {
                AnthropicAnalysisResponse.McpTool mcpTool = new AnthropicAnalysisResponse.McpTool();
                mcpTool.setName(tool.getName());
                mcpTool.setDescription(tool.getDescription());
                mcpTool.setUsage(tool.getInputs() + " " + tool.getOutputs());
                mcpTool.setDocumentation(tool.getInputs() + " " + tool.getOutputs());
                mcpTool.setIsRequired(true);
                mcpTools.add(mcpTool);
            }
        }
        response.setMcpTools(mcpTools);
    }

    private void setEnvironmentVariables(AnthropicAnalysisResponse response, String[] envVars) {
        List<AnthropicAnalysisResponse.EnvironmentVariable> variables = new ArrayList<>();
        if (envVars != null) {
            for (String envVar : envVars) {
                AnthropicAnalysisResponse.EnvironmentVariable variable = new AnthropicAnalysisResponse.EnvironmentVariable();
                variable.setName(envVar);
                variable.setDescription("Environment Variable: " + envVar);
                variable.setRequired(true);
                variable.setType("string");
                variables.add(variable);
            }
        }
        response.setEnvironmentVariables(variables);
    }

    private void setCommands(AnthropicAnalysisResponse response) {
        List<AnthropicAnalysisResponse.Command> commands = new ArrayList<>();
        AnthropicAnalysisResponse.Command command = new AnthropicAnalysisResponse.Command();
        command.setName("Start Server");
        command.setDescription("Start the MCP server");
        command.setCommand("java -jar server.jar");
        commands.add(command);
        response.setCommands(commands);
    }

    private void setDeploymentOptions(AnthropicAnalysisResponse response, String[] deploymentOptions) {
        List<AnthropicAnalysisResponse.DeploymentOption> options = new ArrayList<>();
        if (deploymentOptions != null) {
            for (String option : deploymentOptions) {
                AnthropicAnalysisResponse.DeploymentOption deployOption = new AnthropicAnalysisResponse.DeploymentOption();
                deployOption.setName(option);
                deployOption.setDescription("Deployment Option: " + option);
                deployOption.setInstructions(option);
                deployOption.setPlatform("container");
                options.add(deployOption);
            }
        }
        response.setDeploymentOptions(options);
    }

    private void setSystemRequirements(AnthropicAnalysisResponse response) {
        AnthropicAnalysisResponse.SystemRequirements sysReq = new AnthropicAnalysisResponse.SystemRequirements();
        sysReq.setMinimumCpu("2 cores");
        sysReq.setRecommendedCpu("4 cores");
        sysReq.setMinimumMemory("2GB");
        sysReq.setRecommendedMemory("4GB");
        sysReq.setMinimumStorage("1GB");
        sysReq.setRecommendedStorage("5GB");
        sysReq.setSoftwareRequirements(List.of("Java 17+", "Docker (optional)"));
        response.setSystemRequirements(sysReq);
    }

    private void setDatabase(AnthropicAnalysisResponse response) {
        AnthropicAnalysisResponse.Database db = new AnthropicAnalysisResponse.Database();
        db.setType("MongoDB");
        db.setConnectionString("mongodb://localhost:27017/mcp");
        db.setDescription("Used for storing model metadata");
        db.setDescriptionSchema("db.createCollection('models')");
        response.setDatabase(db);
    }

    @Getter
    public static class RepositoryAnalysisResult {
        private String[] tags;
        private boolean isValidMcpRepo;
        private int score;
        private String analysis;
        private String recommendation;
        private String reason;
        private Tools[] tools;
        private String[] environmentVariables;
        private String[] deploymentOptions;
        private String[] supportedLanguages;
        private String[] platforms;
        private String[] usageExamples;
        private Object citations;
    }

    @Getter
    public static class Tools {
        private String name;
        private String description;
        private String inputs;
        private String outputs;
    }
}