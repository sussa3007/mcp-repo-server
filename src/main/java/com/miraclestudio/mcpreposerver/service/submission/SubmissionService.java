package com.miraclestudio.mcpreposerver.service.submission;

import com.miraclestudio.mcpreposerver.domain.common.Tag;
import com.miraclestudio.mcpreposerver.repository.user.UserRepository;
import com.miraclestudio.mcpreposerver.domain.submission.Submission;
import com.miraclestudio.mcpreposerver.domain.submission.SubmissionTag;
import com.miraclestudio.mcpreposerver.domain.submission.Submission.RepositoryType;
import com.miraclestudio.mcpreposerver.domain.submission.Submission.Status;
import com.miraclestudio.mcpreposerver.domain.user.Role;
import com.miraclestudio.mcpreposerver.domain.user.User;
import com.miraclestudio.mcpreposerver.exception.BusinessException;
import com.miraclestudio.mcpreposerver.exception.ErrorCode;
import com.miraclestudio.mcpreposerver.repository.common.TagRepository;
import com.miraclestudio.mcpreposerver.repository.submission.SubmissionRepository;
import com.miraclestudio.mcpreposerver.service.github.GitHubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.miraclestudio.mcpreposerver.dto.response.SubmissionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final TagRepository tagRepository;
    private final GitHubService githubService;
    private final AsyncSubmissionService asyncSubmissionService;
    private final UserRepository userRepository;

    /**
     * 레포지토리 제출
     *
     * @param name        레포지토리 이름
     * @param author      작성자
     * @param type        레포지토리 유형 (SERVER/CLIENT)
     * @param description 설명
     * @param repoUrl     GitHub URL
     * @param websiteUrl  웹사이트 URL
     * @param tags        태그 (쉼표로 구분)
     * @param email       이메일
     * @param user        사용자 (선택 사항)
     * @return 생성된 제출 ID
     */
    @Transactional
    public SubmissionResponse submitRepository(String userEmail, String name, String author, RepositoryType type,
            String description,
            String repoUrl, String websiteUrl, List<String> tags, String email) {
        // GitHub URL 유효성 검증
        if (!githubService.validateGitHubUrl(repoUrl)) {
            throw new BusinessException(ErrorCode.GITHUB_URL_INVALID);
        }
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        // 제출 생성
        Submission submission = Submission.builder()
                .name(name)
                .author(author)
                .type(type)
                .description(description)
                .repoUrl(repoUrl)
                .repositoryId(0L)
                .websiteUrl(websiteUrl)
                .email(email)
                .status(Status.PENDING)
                .message("Please wait for review.")
                .user(user)
                .build();
        user.addSubmission(submission);
        Submission saveSubmit = submissionRepository.save(submission);

        // 태그 추가
        tags.forEach(tagName -> {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));

            SubmissionTag submissionTag = SubmissionTag.builder()
                    .submission(saveSubmit)
                    .tag(tag)
                    .build();

            saveSubmit.addSubmissionTag(submissionTag);
            tag.addSubmissionTag(submissionTag);

        });

        Submission resultSubmission = submissionRepository.save(saveSubmit);
        submissionRepository.flush();

        try {
            // 비동기로 자동 리뷰 실행 - 별도의 서비스를 통해 호출하여 프록시를 거치도록 함
            log.info("비동기 리뷰 작업 요청: submissionId={}", resultSubmission.getSubmissionId());
            CompletableFuture<Void> future = asyncSubmissionService
                    .reviewSubmission(resultSubmission.getSubmissionId());
            log.info("비동기 리뷰 작업 요청 완료: submissionId={}", resultSubmission.getSubmissionId());
            return SubmissionResponse.from(resultSubmission);
        } catch (Exception e) {
            resultSubmission.updateStatus(Status.REJECTED,
                    "Submission processing error : " + e.getMessage());
            submissionRepository.save(resultSubmission);
            log.error("레포지토리 제출 중 오류 발생: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR,
                    "Submission processing error : " + e.getMessage());
        }
    }

    @Transactional
    public SubmissionResponse reanalyzeRepository(String userEmail, Long submissionId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Submission submission = getSubmissionEntity(submissionId);
        if (!submission.getUser().getEmail().equals(userEmail) && user.getRole() != Role.ADMIN) {
            log.error("재분석 요청 권한 없음: submissionUserEmail={}, requestUserEmail={}, submissionId={}",
                    submission.getUser().getEmail(), userEmail, submissionId);
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        submission.updateStatus(Status.PENDING, "Reanalysis requested.");
        submissionRepository.flush();
        try {
            // 비동기로 자동 리뷰 실행 - 별도의 서비스를 통해 호출하여 프록시를 거치도록 함
            log.info("비동기 리뷰 재작업 요청: submissionId={}", submission.getSubmissionId());
            CompletableFuture<Void> future = asyncSubmissionService
                    .reviewSubmission(submission.getSubmissionId());
            log.info("비동기 리뷰 재작업 요청 완료: submissionId={}", submission.getSubmissionId());
            return SubmissionResponse.from(submission);
        } catch (Exception e) {
            submission.updateStatus(Status.REJECTED,
                    "Submission processing error : " + e.getMessage());
            submissionRepository.save(submission);
            log.error("레포지토리 제출 중 오류 발생: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR,
                    "Submission processing error : " + e.getMessage());
        }
    }

    /**
     * 제출 상태 조회
     *
     * @param submissionId 제출 ID
     * @return 제출 정보
     */
    @Transactional(readOnly = true)
    public SubmissionResponse getSubmission(Long submissionId) {
        return submissionRepository.findById(submissionId)
                .map(SubmissionResponse::from)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUBMISSION_NOT_FOUND));
    }

    @Transactional
    public Submission getSubmissionEntity(Long submissionId) {
        return submissionRepository.findById(submissionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUBMISSION_NOT_FOUND));
    }

    /**
     * 관리자가 제출 상태 수동 업데이트
     *
     * @param submissionId 제출 ID
     * @param status       새로운 상태
     * @param message      상태 변경 메시지
     * @return 업데이트된 제출 정보
     */
    @Transactional
    public SubmissionResponse updateSubmissionStatus(Long submissionId, Status status, String message) {
        Submission submission = getSubmissionEntity(submissionId);

        // 이미 동일한 상태인 경우
        if (submission.getStatus() == status) {
            return SubmissionResponse.from(submission);
        }

        // 승인 시 레포지토리 생성
        if (status == Status.APPROVED && submission.getStatus() != Status.APPROVED) {
            try {
                // 비동기 서비스를 통해 레포지토리 처리 요청
                CompletableFuture<Void> future = asyncSubmissionService.reviewSubmission(submissionId);

                // 상태 업데이트는 비동기 서비스에서 처리하므로 여기서는 대기하지 않음
                // 관리자 요청에 빠르게 응답하기 위해 상태만 변경
                submission.updateStatus(status, message);
                return SubmissionResponse.from(submissionRepository.save(submission));
            } catch (Exception e) {
                log.error("수동 승인 중 오류 발생: {}", e.getMessage(), e);
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR,
                        "Repository creation error : " + e.getMessage());
            }
        }

        // 상태 업데이트
        submission.updateStatus(status, message);
        return SubmissionResponse.from(submissionRepository.save(submission));
    }

    /**
     * 사용자별 제출 목록 조회 (페이징 처리)
     *
     * @param userEmail 사용자 이메일
     * @param pageable  페이지네이션 정보
     * @return 제출 목록 (페이지)
     */
    @Transactional(readOnly = true)
    public Page<SubmissionResponse> getUserSubmissions(String userEmail, Pageable pageable) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Page<Submission> submissionPage = submissionRepository.findByUser(user, pageable);
        return submissionPage.map(SubmissionResponse::from);
    }

    /**
     * 모든 제출 목록 조회 (페이징 처리)
     * 관리자용 API
     *
     * @param pageable 페이지네이션 정보
     * @return 제출 목록 (페이지)
     */
    @Transactional(readOnly = true)
    public Page<SubmissionResponse> getAllSubmissions(Pageable pageable) {
        Page<Submission> submissionPage = submissionRepository.findAll(pageable);
        return submissionPage.map(SubmissionResponse::from);
    }

    /**
     * 상태별 제출 목록 조회 (페이징 처리)
     *
     * @param status   조회할 상태
     * @param pageable 페이지네이션 정보
     * @return 제출 목록 (페이지)
     */
    @Transactional(readOnly = true)
    public Page<SubmissionResponse> getSubmissionsByStatus(Status status, Pageable pageable) {
        Page<Submission> submissionPage = submissionRepository.findByStatus(status, pageable);
        return submissionPage.map(SubmissionResponse::from);
    }

    /**
     * 유형별 제출 목록 조회 (페이징 처리)
     *
     * @param type     레포지토리 유형 (SERVER/CLIENT)
     * @param pageable 페이지네이션 정보
     * @return 제출 목록 (페이지)
     */
    @Transactional(readOnly = true)
    public Page<SubmissionResponse> getSubmissionsByType(RepositoryType type, Pageable pageable) {
        Page<Submission> submissionPage = submissionRepository.findByType(type, pageable);
        return submissionPage.map(SubmissionResponse::from);
    }
}
