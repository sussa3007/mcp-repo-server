package com.miraclestudio.mcpreposerver.repository.server;

import com.miraclestudio.mcpreposerver.domain.repository.server.ServerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 서버 레포지토리의 커스텀 쿼리 메서드를 정의하는 인터페이스
 */
public interface ServerRepositoryCustomRepository {

    /**
     * 여러 조건을 조합하여 서버 레포지토리를 검색
     * 
     * @param language 프로그래밍 언어 필터
     * @param tags     태그 목록 필터
     * @param keyword  검색 키워드 (이름, 설명)
     * @param pageable 페이지네이션 정보
     * @return 검색 결과 페이지
     */
    Page<ServerRepository> searchByCondition(String keyword, Pageable pageable);
}