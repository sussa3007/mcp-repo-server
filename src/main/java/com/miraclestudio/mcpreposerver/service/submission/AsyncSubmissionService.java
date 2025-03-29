package com.miraclestudio.mcpreposerver.service.submission;

import com.miraclestudio.mcpreposerver.domain.common.Tag;
import com.miraclestudio.mcpreposerver.domain.repository.client.ClientRepository;
import com.miraclestudio.mcpreposerver.domain.repository.client.ClientRepositoryTag;
import com.miraclestudio.mcpreposerver.domain.repository.server.DeploymentOption;
import com.miraclestudio.mcpreposerver.domain.repository.server.EnvironmentVariable;
import com.miraclestudio.mcpreposerver.domain.repository.server.McpTool;
import com.miraclestudio.mcpreposerver.domain.repository.server.ServerRepository;
import com.miraclestudio.mcpreposerver.domain.repository.server.ServerRepositoryTag;
import com.miraclestudio.mcpreposerver.domain.submission.Submission;
import com.miraclestudio.mcpreposerver.domain.submission.Submission.Status;
import com.miraclestudio.mcpreposerver.domain.submission.Submission.RepositoryType;
import com.miraclestudio.mcpreposerver.dto.anthropic.AnthropicAnalysisResponse;
import com.miraclestudio.mcpreposerver.dto.github.GitHubReadmeResponse;
import com.miraclestudio.mcpreposerver.dto.github.GitHubRepositoryResponse;
import com.miraclestudio.mcpreposerver.exception.BusinessException;
import com.miraclestudio.mcpreposerver.exception.ErrorCode;
import com.miraclestudio.mcpreposerver.repository.client.ClientRepositoryRepository;
import com.miraclestudio.mcpreposerver.repository.common.TagRepository;
import com.miraclestudio.mcpreposerver.repository.server.ServerRepositoryRepository;
import com.miraclestudio.mcpreposerver.repository.submission.SubmissionRepository;
import com.miraclestudio.mcpreposerver.service.anthropic.AnthropicService;
import com.miraclestudio.mcpreposerver.service.github.GitHubService;
import com.miraclestudio.mcpreposerver.domain.repository.server.ServerContributorMapping;
import com.miraclestudio.mcpreposerver.domain.repository.server.Contributor;
import com.miraclestudio.mcpreposerver.domain.repository.client.ClientContributor;
import com.miraclestudio.mcpreposerver.dto.github.GitHubContributorResponse;
import com.miraclestudio.mcpreposerver.domain.repository.client.ClientContributorMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.miraclestudio.mcpreposerver.repository.server.ContributorRepository;
import com.miraclestudio.mcpreposerver.repository.server.ServerContributorMappingRepository;
import com.miraclestudio.mcpreposerver.repository.client.ClientContributorRepository;
import com.miraclestudio.mcpreposerver.repository.client.ClientContributorMappingRepository;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncSubmissionService {

    private final SubmissionRepository submissionRepository;
    private final ServerRepositoryRepository serverRepositoryRepository;
    private final ClientRepositoryRepository clientRepositoryRepository;
    private final TagRepository tagRepository;
    private final GitHubService githubService;
    private final AnthropicService anthropicService;
    private final ContributorRepository contributorRepository;
    private final ServerContributorMappingRepository serverContributorMappingRepository;
    private final ClientContributorRepository clientContributorRepository;
    private final ClientContributorMappingRepository clientContributorMappingRepository;

    /**
     * 제출 자동 리뷰 (비동기 처리)
     *
     * @param submissionId 제출 ID
     * @return CompletableFuture<Void> 비동기 처리 결과
     */
    @Async("reviewTaskExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CompletableFuture<Void> reviewSubmission(Long submissionId) {
        log.info("비동기 리뷰 작업 시작: submissionId={}, thread={}", submissionId, Thread.currentThread().getName());
        Submission submission = getSubmission(submissionId);

        try {
            Thread.sleep(2000);
            log.info("제출 정보 조회 완료: submissionId={}, status={}", submissionId, submission.getStatus());
            // 이미 존재하는 레포지토리인지 확인
            Optional<ServerRepository> existingServerRepo = serverRepositoryRepository
                    .findByGithubUrl(submission.getRepoUrl());
            Optional<ClientRepository> existingClientRepo = clientRepositoryRepository
                    .findByGithubUrl(submission.getRepoUrl());

            if (existingServerRepo.isPresent() || existingClientRepo.isPresent()) {
                log.error("이미 등록된 레포지토리: {}", submission.getRepoUrl());
                updateSubmissionStatus(submission, Status.REJECTED,
                        "Already registered repository. Please request approval from the administrator.");
                return CompletableFuture.completedFuture(null);

            }

            // 이미 처리된 제출인 경우
            if (submission.getStatus() != Status.PENDING) {
                log.warn("이미 처리된 제출: submissionId={}, status={}", submissionId, submission.getStatus());
                updateSubmissionStatus(submission, Status.REJECTED,
                        "Already processed submission");
                return CompletableFuture.completedFuture(null);

            }

            Long repositoryId = 0L;
            // GitHub URL에서 정보 추출
            String repoUrl = submission.getRepoUrl();
            String[] ownerRepo = githubService.extractOwnerAndRepo(repoUrl);
            String owner = ownerRepo[0];
            String repo = ownerRepo[1];
            log.info("GitHub 레포지토리 정보 추출: owner={}, repo={}", owner, repo);

            // GitHub 레포지토리 정보 조회
            log.debug("GitHub API 호출 시작: owner={}, repo={}", owner, repo);
            GitHubRepositoryResponse repoResponse = githubService.getRepository(owner, repo);
            log.info("GitHub 레포지토리 정보 조회 완료: repoName={}", repoResponse.getName());

            log.debug("GitHub README 조회 시작: repoUrl={}", repoUrl);
            GitHubReadmeResponse readmeResponse = githubService.getReadmeFromUrl(repoUrl);
            String readmeContent = readmeResponse.getDecodedContent();
            log.info("GitHub README 조회 완료: contentLength={}", readmeContent.length());

            // 레포지토리 타입에 따라 분석 요청
            log.info("레포지토리 분석 시작: type={}", submission.getType().name());
            AnthropicAnalysisResponse analysisResult = anthropicService.analyzeRepository(
                    repoResponse, readmeContent, submission.getType().name());
            log.info("레포지토리 분석 완료: recommendation={}", analysisResult.getRecommendation());

            // 분석 결과에 따라 처리
            if ("Approve".equalsIgnoreCase(analysisResult.getRecommendation())) {
                log.info("레포지토리 승인 처리 시작: submissionId={}", submissionId);
                // 승인: 새로운 레포지토리 생성 및 저장
                repositoryId = createRepository(submission, repoResponse, analysisResult);
                log.info("레포지토리 승인 처리 완료: submissionId={}, repositoryId={}", submissionId, repositoryId);

                // 제출 상태 업데이트
                submission.updateStatus(Status.APPROVED,
                        "Analysis Result: " + analysisResult.getAnalysis());
            } else {
                log.info("레포지토리 거부 처리: submissionId={}, reason={}", submissionId, analysisResult.getReason());
                // 거부: 제출 상태 업데이트
                submission.updateStatus(Status.REJECTED,
                        "Rejected Reason: " + analysisResult.getReason() +
                                "\n\nAnalysis Result: " + analysisResult.getAnalysis());
            }

            submission.setRepositoryId(repositoryId);
            submissionRepository.save(submission);
            log.info("제출 리뷰 완료: submissionId={}, status={}", submissionId, submission.getStatus());

        } catch (Exception e) {
            log.error("제출 리뷰 중 오류 발생: submissionId={}, error={}", submissionId, e.getMessage(), e);
            updateSubmissionStatus(submission, Status.REJECTED,
                    "Submission processing error: " + "Please request approval from the administrator.");
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void updateSubmissionStatus(Submission submission, Status status, String message) {
        try {
            submission.updateStatus(status, message);
            submissionRepository.saveAndFlush(submission);
            log.info("제출 상태 업데이트 완료: submissionId={}, status={}, message={}",
                    submission.getSubmissionId(), status, message);
        } catch (Exception e) {
            log.error("제출 상태 업데이트 실패: submissionId={}, error={}",
                    submission.getSubmissionId(), e.getMessage(), e);
            throw new BusinessException(ErrorCode.SUBMISSION_UPDATE_FAILED,
                    "Failed to update submission status: " + e.getMessage());
        }
    }

    /**
     * 제출 상태 조회
     *
     * @param submissionId 제출 ID
     * @return 제출 정보
     */
    @Transactional
    public Submission getSubmission(Long submissionId) {
        return submissionRepository.findById(submissionId)
                .orElseThrow(() -> {
                    log.error("제출 조회 중 오류 발생: submissionId={}", submissionId);
                    throw new BusinessException(ErrorCode.SUBMISSION_NOT_FOUND);
                });
    }

    /**
     * 분석 결과를 기반으로 레포지토리 생성
     *
     * @param submission     제출 정보
     * @param repoResponse   GitHub 레포지토리 정보
     * @param analysisResult 분석 결과
     */
    private Long createRepository(Submission submission, GitHubRepositoryResponse repoResponse,
            AnthropicAnalysisResponse analysisResult) {
        if (submission.getType() == RepositoryType.SERVER) {
            return createServerRepository(submission, repoResponse, analysisResult);
        } else {
            return createClientRepository(submission, repoResponse, analysisResult);
        }
    }

    /**
     * 서버 레포지토리 생성
     */
    private Long createServerRepository(Submission submission, GitHubRepositoryResponse repoResponse,
            AnthropicAnalysisResponse analysisResult) {

        // 서버 레포지토리 생성
        ServerRepository serverRepository = ServerRepository.builder()
                .name(submission.getName())
                .description(submission.getDescription())
                .owner(repoResponse.getOwner().getLogin())
                .repo(repoResponse.getName())
                .githubUrl(submission.getRepoUrl())
                .demoUrl(submission.getWebsiteUrl())
                .isOfficial(analysisResult.getIsOfficial())
                .language(repoResponse.getLanguage() != null ? repoResponse.getLanguage() : "Unknown")
                .stars(repoResponse.getStars() != null ? repoResponse.getStars() : 0)
                .forks(repoResponse.getForks() != null ? repoResponse.getForks() : 0)
                .license(repoResponse.getLicense() != null ? repoResponse.getLicense().getName() : "Unknown")
                .build();
        ServerRepository saveServerRepository = serverRepositoryRepository.save(serverRepository);

        // 태그 추가
        analysisResult.getTags().forEach(tagName -> {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));

            ServerRepositoryTag serverRepositoryTag = ServerRepositoryTag.builder()
                    .serverRepository(saveServerRepository)
                    .tag(tag)
                    .build();

            saveServerRepository.addServerRepositoryTag(serverRepositoryTag);
            tag.addServerRepositoryTag(serverRepositoryTag);
        });

        // MCP Tools 추가
        if (analysisResult.getMcpTools() != null) {
            analysisResult.getMcpTools().forEach(tool -> {
                McpTool mcpTool = McpTool.builder()
                        .name(tool.getName())
                        .description(tool.getDescription())
                        .usageInfo(tool.getUsage())
                        .documentation(tool.getDocumentation())
                        .isRequired(tool.getIsRequired())
                        .build();
                saveServerRepository.addMcpTool(mcpTool);
            });
        }

        // 환경 변수 추가
        if (analysisResult.getEnvironmentVariables() != null) {
            analysisResult.getEnvironmentVariables().forEach(envVar -> {
                EnvironmentVariable environmentVariable = EnvironmentVariable.builder()
                        .name(envVar.getName())
                        .description(envVar.getDescription())
                        .required(envVar.getRequired())
                        .defaultValue(envVar.getDefaultValue())
                        .type(envVar.getType())
                        .build();
                saveServerRepository.addEnvironmentVariable(environmentVariable);
            });
        }

        // 배포 옵션 추가
        if (analysisResult.getDeploymentOptions() != null) {
            analysisResult.getDeploymentOptions().forEach(option -> {
                DeploymentOption deploymentOption = DeploymentOption.builder()
                        .name(option.getName())
                        .description(option.getDescription())
                        .build();
                saveServerRepository.addDeploymentOption(deploymentOption);
            });
        }

        List<GitHubContributorResponse> contributors = githubService.getContributors(repoResponse.getOwner().getLogin(),
                repoResponse.getName());
        log.debug("Starting to process {} contributors for repository: {}", contributors.size(),
                saveServerRepository.getName());

        int processedCount = 0;
        for (GitHubContributorResponse contributor : contributors) {
            try {
                log.debug("Processing contributor {}/{}: {}", ++processedCount, contributors.size(),
                        contributor.getLogin());

                Contributor contributorEntity = Contributor.createContributor(contributor);
                // 컨트리뷰터 엔티티 먼저 저장
                contributorEntity = contributorRepository.save(contributorEntity);
                log.debug("Contributor entity saved successfully: {}", contributorEntity.getUsername());

                ServerContributorMapping serverContributorMapping = ServerContributorMapping.builder()
                        .serverRepository(saveServerRepository)
                        .contributor(contributorEntity)
                        .build();

                saveServerRepository.addContributor(serverContributorMapping);
                contributorEntity.addServerRepository(serverContributorMapping);
                // 매핑 엔티티 저장
                serverContributorMapping = serverContributorMappingRepository.save(serverContributorMapping);
                log.debug("Mapping entity saved successfully with ID: {}",
                        serverContributorMapping.getServerContributorMappingId());

                log.debug("Contributor mapping added to repository successfully");
            } catch (Exception e) {
                log.error("Failed to process contributor {}: {}", contributor.getLogin(), e.getMessage(), e);
                throw new BusinessException(ErrorCode.CONTRIBUTOR_SAVE_ERROR,
                        String.format("Failed to save contributor %s: %s", contributor.getLogin(), e.getMessage()));
            }
        }

        log.info("Successfully processed all {} contributors for repository: {}", contributors.size(),
                saveServerRepository.getName());

        return serverRepositoryRepository.save(saveServerRepository).getServerRepositoryId();
    }

    /**
     * 클라이언트 레포지토리 생성
     */
    private Long createClientRepository(Submission submission, GitHubRepositoryResponse repoResponse,
            AnthropicAnalysisResponse analysisResult) {

        // 클라이언트 레포지토리 생성
        ClientRepository clientRepository = ClientRepository.builder()
                .name(submission.getName())
                .description(submission.getDescription())
                .owner(repoResponse.getOwner().getLogin())
                .repo(repoResponse.getName())
                .githubUrl(submission.getRepoUrl())
                .isOfficial(analysisResult.getIsOfficial())
                .language(repoResponse.getLanguage() != null ? repoResponse.getLanguage() : "Unknown")
                .stars(repoResponse.getStars() != null ? repoResponse.getStars() : 0)
                .forks(repoResponse.getForks() != null ? repoResponse.getForks() : 0)
                .license(repoResponse.getLicense() != null ? repoResponse.getLicense().getName() : "Unknown")
                .installInstructions(analysisResult.getInstallInstructions())
                .build();
        ClientRepository saveClientRepository = clientRepositoryRepository.save(clientRepository);

        // 태그 추가
        analysisResult.getTags().forEach(tagName -> {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));

            ClientRepositoryTag clientRepositoryTag = ClientRepositoryTag.builder()
                    .clientRepository(saveClientRepository)
                    .tag(tag)
                    .build();

            saveClientRepository.addClientRepositoryTag(clientRepositoryTag);
            tag.addClientRepositoryTag(clientRepositoryTag);
        });

        // 지원 언어 추가
        if (analysisResult.getSupportedLanguages() != null) {
            analysisResult.getSupportedLanguages().forEach(language -> {
                saveClientRepository.addSupportedLanguage(language);
            });
        }

        // 지원 플랫폼 추가
        if (analysisResult.getPlatforms() != null) {
            analysisResult.getPlatforms().forEach(platform -> {
                saveClientRepository.addPlatform(platform);
            });
        }

        // 사용 예제 추가
        if (analysisResult.getUsageExamples() != null) {
            analysisResult.getUsageExamples().forEach(example -> {
                saveClientRepository.addUsageExample(example);
            });
        }

        List<GitHubContributorResponse> contributors = githubService.getContributors(repoResponse.getOwner().getLogin(),
                repoResponse.getName());
        log.debug("Starting to process {} contributors for repository: {}", contributors.size(),
                saveClientRepository.getName());

        int processedCount = 0;
        for (GitHubContributorResponse contributor : contributors) {
            try {
                log.debug("Processing contributor {}/{}: {}", ++processedCount, contributors.size(),
                        contributor.getLogin());

                ClientContributor clientContributor = ClientContributor.createContributor(contributor);
                // 컨트리뷰터 엔티티 먼저 저장
                clientContributor = clientContributorRepository.save(clientContributor);
                log.debug("Client contributor entity saved successfully: {}", clientContributor.getUsername());

                ClientContributorMapping clientContributorMapping = ClientContributorMapping.builder()
                        .clientRepository(saveClientRepository)
                        .contributor(clientContributor)
                        .build();

                saveClientRepository.addContributor(clientContributorMapping);
                clientContributor.addClientRepository(clientContributorMapping);
                // 매핑 엔티티 저장
                clientContributorMapping = clientContributorMappingRepository.save(clientContributorMapping);
                log.debug("Client mapping entity saved successfully with ID: {}",
                        clientContributorMapping.getClientContributorMappingId());

                log.debug("Client contributor mapping added to repository successfully");
            } catch (Exception e) {
                log.error("Failed to process client contributor {}: {}", contributor.getLogin(), e.getMessage(), e);
                throw new BusinessException(ErrorCode.CONTRIBUTOR_SAVE_ERROR,
                        String.format("Failed to save client contributor %s: %s", contributor.getLogin(),
                                e.getMessage()));
            }
        }

        log.info("Successfully processed all {} contributors for repository: {}", contributors.size(),
                saveClientRepository.getName());

        return clientRepositoryRepository.save(saveClientRepository).getClientRepositoryId();
    }
}