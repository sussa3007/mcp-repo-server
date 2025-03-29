package com.miraclestudio.mcpreposerver.service.repository;

import com.miraclestudio.mcpreposerver.dto.response.repository.ClientRepositoryResponseDto;
import com.miraclestudio.mcpreposerver.dto.response.repository.ClientRepositorySimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 클라이언트 레포지토리 관련 서비스 인터페이스
 * 클라이언트 레포지토리 조회, 검색 및 상세 정보 제공 기능을 정의합니다.
 */
public interface ClientRepositoryService {

    /**
     * 모든 클라이언트 레포지토리 목록을 페이징하여 조회합니다.
     *
     * @param pageable 페이지 정보 (페이지 번호, 크기, 정렬 조건 등)
     * @return 페이징된 클라이언트 레포지토리 응답 DTO 목록
     */
    Page<ClientRepositorySimpleResponseDto> getClientRepositories(String keyword, Pageable pageable);

    /**
     * 특정 ID의 클라이언트 레포지토리를 상세 조회합니다.
     *
     * @param id 조회할 클라이언트 레포지토리 ID
     * @return 클라이언트 레포지토리 응답 DTO
     */
    ClientRepositoryResponseDto getClientRepository(Long id);

    /**
     * 클라이언트 레포지토리를 다양한 조건으로 필터링하여 검색합니다.
     *
     * @param language  프로그래밍 언어 필터 (null 가능)
     * @param tags      태그 목록 필터 (null 가능)
     * @param keyword   검색 키워드 (이름, 설명 등에서 검색, null 가능)
     * @param platforms 지원하는 플랫폼 목록 필터 (null 가능)
     * @param pageable  페이지 정보 (페이지 번호, 크기, 정렬 조건 등)
     * @return 필터링된 클라이언트 레포지토리 응답 DTO 목록
     */
    Page<ClientRepositorySimpleResponseDto> searchClientRepositories(String keyword, Pageable pageable);

    /**
     * GitHub 소유자와 레포지토리 이름으로 클라이언트 레포지토리를 조회합니다.
     *
     * @param owner GitHub 레포지토리 소유자
     * @param repo  GitHub 레포지토리 이름
     * @return 클라이언트 레포지토리 응답 DTO
     */
    ClientRepositoryResponseDto getClientRepositoryByOwnerAndRepo(String owner, String repo);
}

