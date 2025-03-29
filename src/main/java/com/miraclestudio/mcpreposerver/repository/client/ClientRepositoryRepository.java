package com.miraclestudio.mcpreposerver.repository.client;

import com.miraclestudio.mcpreposerver.domain.repository.client.ClientRepository;
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
public interface ClientRepositoryRepository
        extends JpaRepository<ClientRepository, Long>, ClientRepositoryCustomRepository {

    /**
     * 단일 클라이언트 레포지토리 조회 (연관 데이터 포함)
     * 클라이언트 레포지토리와 함께 태그, 지원 언어, 지원 플랫폼, 기여자 정보를 함께 조회
     *
     * @param id 클라이언트 레포지토리 ID
     * @return 클라이언트 레포지토리 Optional 객체
     */
    @EntityGraph(attributePaths = {
            "clientRepositoryTags",
            "clientRepositoryTags.tag",
            "supportedLanguages",
            "platforms",
            "usageExamples",
            "contributorMappings",
            "contributorMappings.contributor"
    })
    @Query("SELECT c FROM ClientRepository c WHERE c.clientRepositoryId = :id")
    Optional<ClientRepository> findByIdWithDetails(@Param("id") Long id);

    /**
     * 이름 또는 설명에 키워드가 포함된 클라이언트 레포지토리 목록 조회
     *
     * @param nameKeyword 이름 검색 키워드
     * @param descKeyword 설명 검색 키워드
     * @param pageable    페이지네이션 정보
     * @return
     */
    @EntityGraph(attributePaths = {
            "clientRepositoryTags",
            "clientRepositoryTags.tag"
    })
    Page<ClientRepository> findByNameContainingOrDescriptionContaining(String nameKeyword, String descKeyword,
            Pageable pageable);

    /**
     * 소유자와 레포지토리 이름으로 클라이언트 레포지토리 조회
     *
     * @param owner GitHub 레포지토리 소유자
     * @param repo  GitHub 레포지토리 이름
     * @return 클라이언트 레포지토리 Optional 객체
     */
    @EntityGraph(attributePaths = {
            "clientRepositoryTags",
            "clientRepositoryTags.tag",
            "supportedLanguages",
            "platforms",
            "usageExamples",
            "contributorMappings",
            "contributorMappings.contributor"
    })
    Optional<ClientRepository> findByOwnerAndRepo(String owner, String repo);

    /**
     * 모든 클라이언트 레포지토리 목록 조회 (기본 정보만)
     *
     * @param pageable 페이지네이션 정보
     * @return 클라이언트 레포지토리 목록 페이지
     */
    @EntityGraph(attributePaths = {
            "clientRepositoryTags",
            "clientRepositoryTags.tag"
    })
    @Override
    Page<ClientRepository> findAll(Pageable pageable);

    /**
     * GitHub URL로 클라이언트 레포지토리 조회
     *
     * @param githubUrl GitHub URL
     * @return 클라이언트 레포지토리 Optional 객체
     */
    Optional<ClientRepository> findByGithubUrl(String githubUrl);
}