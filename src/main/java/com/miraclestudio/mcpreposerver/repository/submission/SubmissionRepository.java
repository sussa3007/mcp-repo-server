package com.miraclestudio.mcpreposerver.repository.submission;

import com.miraclestudio.mcpreposerver.domain.submission.Submission;
import com.miraclestudio.mcpreposerver.domain.submission.Submission.RepositoryType;
import com.miraclestudio.mcpreposerver.domain.submission.Submission.Status;
import com.miraclestudio.mcpreposerver.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    /**
     * 사용자별 제출 목록 조회
     * 
     * @param user     사용자
     * @param pageable 페이지네이션 정보
     * @return 제출 목록 (페이지)
     */
    Page<Submission> findByUser(User user, Pageable pageable);

    /**
     * 상태별 제출 목록 조회
     * 
     * @param status   상태
     * @param pageable 페이지네이션 정보
     * @return 제출 목록 (페이지)
     */
    Page<Submission> findByStatus(Status status, Pageable pageable);

    /**
     * 레포지토리 유형별 제출 목록 조회
     * 
     * @param type     레포지토리 유형
     * @param pageable 페이지네이션 정보
     * @return 제출 목록 (페이지)
     */
    Page<Submission> findByType(RepositoryType type, Pageable pageable);

    /**
     * 이메일로 제출 목록 조회
     * 
     * @param email 이메일
     * @return 제출 목록
     */
    List<Submission> findByEmail(String email);

    /**
     * GitHub URL로 제출 조회
     * 
     * @param repoUrl GitHub URL
     * @return 제출 (Optional)
     */
    Optional<Submission> findByRepoUrl(String repoUrl);

    /**
     * 특정 기간 내 제출 목록 조회
     * 
     * @param start    시작 일시
     * @param end      종료 일시
     * @param pageable 페이지네이션 정보
     * @return 제출 목록 (페이지)
     */
    Page<Submission> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}