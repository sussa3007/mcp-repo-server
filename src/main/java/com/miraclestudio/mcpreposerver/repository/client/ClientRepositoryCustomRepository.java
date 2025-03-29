package com.miraclestudio.mcpreposerver.repository.client;

import com.miraclestudio.mcpreposerver.domain.repository.client.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 클라이언트 레포지토리의 커스텀 쿼리 메서드를 정의하는 인터페이스
 */
public interface ClientRepositoryCustomRepository {

    /**
     * 여러 조건을 조합하여 클라이언트 레포지토리를 검색
     * 
     * @param keyword   검색 키워드 (이름, 설명)
     * @param platforms 지원 플랫폼 목록 필터
     * @param pageable  페이지네이션 정보
     * @return 검색 결과 페이지
     */
    Page<ClientRepository> searchByCondition(String keyword, Pageable pageable);
}