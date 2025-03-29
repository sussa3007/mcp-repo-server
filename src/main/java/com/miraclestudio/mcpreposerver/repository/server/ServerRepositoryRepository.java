package com.miraclestudio.mcpreposerver.repository.server;

import com.miraclestudio.mcpreposerver.domain.repository.server.ServerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServerRepositoryRepository
        extends JpaRepository<ServerRepository, Long>, ServerRepositoryCustomRepository {

    /**
     * 단일 서버 레포지토리 조회 (연관 데이터 포함)
     * 서버 레포지토리와 함께 태그, 환경변수, 명령어, 배포 옵션, 기여자 정보를 함께 조회
     *
     * @param id 서버 레포지토리 ID
     * @return 서버 레포지토리 Optional 객체
     */
    @EntityGraph(attributePaths = {
            "serverRepositoryTags",
            "serverRepositoryTags.tag",
            "environmentVariables",
            "commands",
            "deploymentOptions",
            "contributorMappings",
            "contributorMappings.contributor"
    })
    @Query("SELECT s FROM ServerRepository s WHERE s.serverRepositoryId = :id")
    Optional<ServerRepository> findByIdWithDetails(@Param("id") Long id);

    /**
     * 이름 또는 설명에 키워드가 포함된 서버 레포지토리 목록 조회
     *
     * @param nameKeyword 이름 검색 키워드
     * @param descKeyword 설명 검색 키워드
     * @param pageable    페이지네이션 정보
     * @return 검색 결과 페이지
     */
    @EntityGraph(attributePaths = {
            "serverRepositoryTags",
            "serverRepositoryTags.tag"
    })
    Page<ServerRepository> findByNameContainingOrDescriptionContaining(String nameKeyword, String descKeyword,
            Pageable pageable);

    /**
     * 소유자와 레포지토리 이름으로 서버 레포지토리 조회
     *
     * @param owner GitHub 레포지토리 소유자
     * @param repo  GitHub 레포지토리 이름
     * @return 서버 레포지토리 Optional 객체
     */
    @EntityGraph(attributePaths = {
            "serverRepositoryTags",
            "serverRepositoryTags.tag",
            "environmentVariables",
            "commands",
            "deploymentOptions",
            "contributorMappings",
            "contributorMappings.contributor"
    })
    Optional<ServerRepository> findByOwnerAndRepo(String owner, String repo);

    /**
     * 모든 서버 레포지토리 목록 조회 (기본 정보만)
     *
     * @param pageable 페이지네이션 정보
     * @return 서버 레포지토리 목록 페이지
     */
    @EntityGraph(attributePaths = {
            "serverRepositoryTags",
            "serverRepositoryTags.tag"
    })
    @Override
    Page<ServerRepository> findAll(Pageable pageable);

    /**
     * GitHub URL로 서버 레포지토리 조회
     *
     * @param githubUrl GitHub URL
     * @return 서버 레포지토리 Optional 객체
     */
    Optional<ServerRepository> findByGithubUrl(String githubUrl);
}