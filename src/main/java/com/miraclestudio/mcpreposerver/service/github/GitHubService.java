package com.miraclestudio.mcpreposerver.service.github;

import com.miraclestudio.mcpreposerver.dto.github.GitHubContentResponse;
import com.miraclestudio.mcpreposerver.dto.github.GitHubReadmeResponse;
import com.miraclestudio.mcpreposerver.dto.github.GitHubRepositoryResponse;
import com.miraclestudio.mcpreposerver.dto.github.GitHubContributorResponse;
import com.miraclestudio.mcpreposerver.exception.BusinessException;
import com.miraclestudio.mcpreposerver.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubService {

    private final WebClient githubWebClient;
    private static final String GITHUB_BASE_URL = "https://github.com/";

    /**
     * GitHub 레포지토리 정보 조회
     *
     * @param owner 레포지토리 소유자
     * @param repo  레포지토리 이름
     * @return 레포지토리 정보
     */
    public GitHubRepositoryResponse getRepository(String owner, String repo) {
        return executeGitHubRequest("/repos/{owner}/{repo}",
                GitHubRepositoryResponse.class,
                owner, repo);
    }

    /**
     * GitHub 레포지토리 README 파일 조회
     *
     * @param owner 레포지토리 소유자
     * @param repo  레포지토리 이름
     * @return README 파일 정보 (Base64로 인코딩된 내용 포함)
     */
    public GitHubReadmeResponse getReadme(String owner, String repo) {
        return executeGitHubRequest("/repos/{owner}/{repo}/readme",
                GitHubReadmeResponse.class,
                owner, repo);
    }

    /**
     * GitHub URL의 형식을 검증하고 README 데이터를 가져옵니다.
     * 
     * @param githubUrl GitHub URL (기본 레포지토리 또는 하위 디렉토리)
     * @return GitHubReadmeResponse 객체
     */
    public GitHubReadmeResponse getReadmeFromUrl(String githubUrl) {
        log.info("GitHub URL에서 README 데이터 가져오기: {}", githubUrl);

        // 기본 정보 추출: 소유자, 레포지토리, 경로
        GitHubUrlInfo urlInfo = parseGitHubUrl(githubUrl);
        String owner = urlInfo.getOwner();
        String repo = urlInfo.getRepo();
        String path = urlInfo.getPath();

        // 소유자와 레포지토리 이름으로 기본 검증
        try {
            // 레포지토리 유효성 검증
            GitHubRepositoryResponse repository = getRepository(owner, repo);
            log.info("유효한 레포지토리: {}/{}, 이름: {}", owner, repo, repository.getName());

            // README 가져오기
            if (path == null || path.isEmpty()) {
                // 기본 레포지토리 README
                return getReadme(owner, repo);
            } else {
                // 특정 경로의 README
                GitHubContentResponse[] contents = getContents(owner, repo, path);

                // README.md 파일 찾기
                for (GitHubContentResponse content : contents) {
                    if (content.getName().equalsIgnoreCase("README.md")) {
                        // README.md 파일 내용 가져오기
                        GitHubContentResponse readmeContent = getContent(owner, repo, content.getPath());
                        if (readmeContent != null && readmeContent.getContent() != null) {
                            // GitHubReadmeResponse 객체로 변환
                            return convertToReadmeResponse(readmeContent);
                        }
                    }
                }

                throw new BusinessException(ErrorCode.GITHUB_README_NOT_FOUND);
            }
        } catch (BusinessException e) {
            log.error("GitHub README 가져오기 실패: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * GitHubContentResponse를 GitHubReadmeResponse로 변환
     */
    private GitHubReadmeResponse convertToReadmeResponse(GitHubContentResponse content) {
        GitHubReadmeResponse response = new GitHubReadmeResponse();
        response.setName(content.getName());
        response.setPath(content.getPath());
        response.setContent(content.getContent());
        response.setSha(content.getSha());
        response.setHtmlUrl(content.getHtmlUrl());
        response.setDownloadUrl(content.getDownloadUrl());
        response.setGitUrl(content.getGitUrl());
        response.setType(content.getType());
        response.setEncoding(content.getEncoding());
        return response;
    }

    /**
     * GitHub URL 정보를 담는 내부 클래스
     */
    private static class GitHubUrlInfo {
        private final String owner;
        private final String repo;
        private final String path;
        private final String branch;

        public GitHubUrlInfo(String owner, String repo, String path, String branch) {
            this.owner = owner;
            this.repo = repo;
            this.path = path;
            this.branch = branch;
        }

        public String getOwner() {
            return owner;
        }

        public String getRepo() {
            return repo;
        }

        public String getPath() {
            return path;
        }

        public String getBranch() {
            return branch;
        }
    }

    /**
     * GitHub URL 파싱
     */
    private GitHubUrlInfo parseGitHubUrl(String githubUrl) {
        if (githubUrl == null || githubUrl.isEmpty()) {
            log.error("GitHub URL이 비어있습니다.");
            throw new BusinessException(ErrorCode.GITHUB_URL_INVALID);
        }

        // 기본 GitHub URL 패턴 검증
        if (!githubUrl.startsWith(GITHUB_BASE_URL)) {
            log.error("GitHub URL 형식이 아닙니다: {}", githubUrl);
            throw new BusinessException(ErrorCode.GITHUB_URL_INVALID);
        }

        String path = githubUrl.substring(GITHUB_BASE_URL.length());
        String[] parts = path.split("/");

        // 최소 소유자와 레포지토리는 있어야 함
        if (parts.length < 2) {
            log.error("소유자와 레포지토리 정보가 부족합니다: {}", githubUrl);
            throw new BusinessException(ErrorCode.GITHUB_URL_INVALID);
        }

        String owner = parts[0];
        String repo = parts[1];
        String subPath = null;
        String branch = null;

        // "tree/HEAD" 또는 "tree/브랜치명" 패턴 확인
        if (parts.length > 3 && parts[2].equals("tree") && parts.length > 4) {
            branch = parts[3];

            // 브랜치 이름 이후의 경로 구성
            StringBuilder pathBuilder = new StringBuilder();
            for (int i = 4; i < parts.length; i++) {
                pathBuilder.append(parts[i]);
                if (i < parts.length - 1) {
                    pathBuilder.append("/");
                }
            }
            subPath = pathBuilder.toString();
        } else if (parts.length > 2) {
            // tree/ 패턴이 없는 경우, 나머지 경로 구성
            StringBuilder pathBuilder = new StringBuilder();
            for (int i = 2; i < parts.length; i++) {
                pathBuilder.append(parts[i]);
                if (i < parts.length - 1) {
                    pathBuilder.append("/");
                }
            }
            subPath = pathBuilder.toString();
        }

        return new GitHubUrlInfo(owner, repo, subPath, branch);
    }

    /**
     * GitHub 레포지토리 내용 조회
     *
     * @param owner 레포지토리 소유자
     * @param repo  레포지토리 이름
     * @param path  조회할 경로 (루트 경로는 빈 문자열 또는 null)
     * @return 레포지토리 내용 (디렉토리인 경우 배열)
     */
    public GitHubContentResponse[] getContents(String owner, String repo, String path) {
        PathAndBranch pathInfo = parsePath(path);
        String actualPath = pathInfo.getPath();
        String branch = pathInfo.getBranch();

        // API 호출을 위한 URI 구성
        String uri = getContentUri(actualPath);

        // 브랜치가 지정된 경우와 그렇지 않은 경우 처리
        if (branch != null) {
            return executeGitHubRequestWithParams(uri + "?ref={branch}",
                    GitHubContentResponse[].class,
                    owner, repo, actualPath, branch);
        } else {
            return executeGitHubRequest(uri, GitHubContentResponse[].class, owner, repo, actualPath);
        }
    }

    /**
     * GitHub 레포지토리의 단일 파일 내용 조회
     *
     * @param owner 레포지토리 소유자
     * @param repo  레포지토리 이름
     * @param path  조회할 파일 경로
     * @return 파일 내용
     */
    public GitHubContentResponse getContent(String owner, String repo, String path) {
        PathAndBranch pathInfo = parsePath(path);
        String actualPath = pathInfo.getPath();
        String branch = pathInfo.getBranch();

        // API 호출을 위한 URI 구성
        String uri = getContentUri(actualPath);

        // 브랜치가 지정된 경우와 그렇지 않은 경우 처리
        if (branch != null) {
            return executeGitHubRequestWithParams(uri + "?ref={branch}",
                    GitHubContentResponse.class,
                    owner, repo, actualPath, branch);
        } else {
            return executeGitHubRequest(uri, GitHubContentResponse.class, owner, repo, actualPath);
        }
    }

    /**
     * 경로 문자열을 기반으로 콘텐츠 API URI 생성
     */
    private String getContentUri(String path) {
        return (path == null || path.isEmpty())
                ? "/repos/{owner}/{repo}/contents"
                : "/repos/{owner}/{repo}/contents/{path}";
    }

    /**
     * 경로 문자열에서 실제 경로와 브랜치 정보 파싱
     */
    private PathAndBranch parsePath(String path) {
        if (path == null || path.isEmpty()) {
            return new PathAndBranch("", null);
        }

        // tree/ 패턴 확인
        if (path.startsWith("tree/")) {
            String[] pathParts = path.split("/", 3); // "tree", "{branch}", "{originPath}"로 분할

            if (pathParts.length >= 2) {
                String branch = pathParts[1]; // 브랜치 이름 추출
                // 실제 경로 추출 (있을 경우)
                String actualPath = pathParts.length >= 3 ? pathParts[2] : "";
                return new PathAndBranch(actualPath, branch);
            }
        }

        return new PathAndBranch(path, null);
    }

    /**
     * 경로와 브랜치 정보를 저장하는 내부 클래스
     */
    private static class PathAndBranch {
        private final String path;
        private final String branch;

        public PathAndBranch(String path, String branch) {
            this.path = path;
            this.branch = branch;
        }

        public String getPath() {
            return path;
        }

        public String getBranch() {
            return branch;
        }
    }

    /**
     * GitHub URL 유효성 검증
     *
     * @param githubUrl GitHub URL
     * @return 유효성 여부
     */
    public boolean validateGitHubUrl(String githubUrl) {
        log.info("GitHub URL 검증: {}", githubUrl);

        if (githubUrl == null || githubUrl.isEmpty()) {
            log.info("GitHub URL이 비어있습니다.");
            return false;
        }

        try {
            GitHubUrlInfo urlInfo = parseGitHubUrl(githubUrl);
            getRepository(urlInfo.getOwner(), urlInfo.getRepo());
            return true;
        } catch (BusinessException e) {
            log.error("GitHub URL 검증 실패: {}", e.getMessage());
            return false;
        }
    }

    /**
     * GitHub URL에서 소유자와 레포지토리 이름 추출
     *
     * @param githubUrl GitHub URL
     * @return 소유자와 레포지토리 이름 [0] = owner, [1] = repo
     */
    public String[] extractOwnerAndRepo(String githubUrl) {
        GitHubUrlInfo urlInfo = parseGitHubUrl(githubUrl);
        return new String[] { urlInfo.getOwner(), urlInfo.getRepo() };
    }

    /**
     * GitHub 레포지토리의 Contributor 목록 조회
     *
     * @param owner 레포지토리 소유자
     * @param repo  레포지토리 이름
     * @return Contributor 목록
     */
    public List<GitHubContributorResponse> getContributors(String owner, String repo) {
        return executeGitHubRequestWithType(
                "/repos/{owner}/{repo}/contributors",
                new ParameterizedTypeReference<List<GitHubContributorResponse>>() {
                },
                owner, repo);
    }

    /**
     * 클래스 타입을 사용하는 GitHub API 요청 실행
     */
    private <T> T executeGitHubRequest(String uri, Class<T> responseType, Object... uriVariables) {
        return githubWebClient.get()
                .uri(uri, uriVariables)
                .retrieve()
                .onStatus(
                        status -> status.equals(HttpStatus.NOT_FOUND),
                        response -> Mono.error(new BusinessException(ErrorCode.GITHUB_REPO_NOT_FOUND)))
                .bodyToMono(responseType)
                .onErrorResume(this::handleWebClientError)
                .block();
    }

    /**
     * 추가 쿼리 파라미터가 있는 GitHub API 요청 실행
     */
    private <T> T executeGitHubRequestWithParams(String uri, Class<T> responseType, Object... uriVariables) {
        return githubWebClient.get()
                .uri(uri, uriVariables)
                .retrieve()
                .onStatus(
                        status -> status.equals(HttpStatus.NOT_FOUND),
                        response -> Mono.error(new BusinessException(ErrorCode.GITHUB_REPO_NOT_FOUND)))
                .bodyToMono(responseType)
                .onErrorResume(this::handleWebClientError)
                .block();
    }

    /**
     * ParameterizedTypeReference를 사용하는 GitHub API 요청 실행
     */
    private <T> T executeGitHubRequestWithType(String uri, ParameterizedTypeReference<T> responseType,
            Object... uriVariables) {
        log.debug("GitHub API 요청: {}", uri);
        return githubWebClient.get()
                .uri(uri, uriVariables)
                .retrieve()
                .onStatus(
                        status -> status.equals(HttpStatus.NOT_FOUND),
                        response -> Mono.error(new BusinessException(ErrorCode.GITHUB_REPO_NOT_FOUND)))
                .bodyToMono(responseType)
                .onErrorResume(this::handleWebClientError)
                .block();
    }

    /**
     * WebClient 에러 처리
     */
    private <T> Mono<T> handleWebClientError(Throwable error) {
        if (error instanceof WebClientResponseException) {
            WebClientResponseException e = (WebClientResponseException) error;
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                return Mono.error(new BusinessException(ErrorCode.GITHUB_INVALID_TOKEN));
            } else if (e.getStatusCode().equals(HttpStatus.FORBIDDEN) &&
                    e.getResponseBodyAsString().contains("rate limit")) {
                return Mono.error(new BusinessException(ErrorCode.GITHUB_RATE_LIMIT_EXCEEDED));
            }
            return Mono.error(new BusinessException(ErrorCode.GITHUB_API_ERROR, e.getMessage()));
        }
        return Mono.error(error);
    }
}