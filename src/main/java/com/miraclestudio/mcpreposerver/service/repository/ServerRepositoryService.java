package com.miraclestudio.mcpreposerver.service.repository;

import com.miraclestudio.mcpreposerver.dto.response.repository.ServerRepositorySimpleResponseDto;
import com.miraclestudio.mcpreposerver.dto.response.repository.ServerRepositoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 서버 레포지토리 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 */
public interface ServerRepositoryService {

    /**
     * 모든 서버 레포지토리 목록을 페이징 처리하여 조회합니다.
     * 
     * @param pageable 페이지네이션 정보 (페이지 번호, 크기, 정렬 조건)
     * @return 서버 레포지토리 목록 (페이지 객체)
     */
    Page<ServerRepositorySimpleResponseDto> getServerRepositories(String keyword, Pageable pageable);

    /**
     * ID를 기반으로 특정 서버 레포지토리의 상세 정보를 조회합니다.
     * 
     * @param id 서버 레포지토리 ID
     * @return 서버 레포지토리 상세 정보
     */
    ServerRepositoryResponseDto getServerRepository(Long id);

    /**
     * 조건에 따라 서버 레포지토리를 필터링하고 검색합니다.
     * 
     * @param language 프로그래밍 언어 필터 (null 가능)
     * @param tags 태그 목록 필터 (null 가능)
     * @param keyword 검색 키워드 (이름, 설명에서 검색, null 가능)
    * @param pageable 페이지네이션 정보
     * @return 필터링된 서버 레포지토리 목록 (페이지 객체)
     */
    Page<ServerRepositorySimpleResponseDto> searchServerRepositories(String keyword, Pageable pageable);

    /**
     * GitHub 소유자와 레포지토리 이름으로 서버 레포지토리를 조회합니다.
     * 
     * @param owner GitHub 레포지토리 소유자 (사용자 또는 조직명)
     * @param repo GitHub 레포지토리 이름
     * @return 서버 레포지토리 정보
     */
    ServerRepositoryResponseDto getServerRepositoryByOwnerAndRepo(String owner, String repo);
}

