package com.miraclestudio.mcpreposerver.repository.client.impl;

import com.miraclestudio.mcpreposerver.domain.repository.client.ClientRepository;
import com.miraclestudio.mcpreposerver.domain.repository.client.QClientRepository;
import com.miraclestudio.mcpreposerver.domain.repository.client.QClientRepositoryTag;
import com.miraclestudio.mcpreposerver.domain.common.QTag;
import com.miraclestudio.mcpreposerver.repository.client.ClientRepositoryCustomRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class ClientRepositoryCustomRepositoryImpl extends QuerydslRepositorySupport
        implements ClientRepositoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public ClientRepositoryCustomRepositoryImpl(EntityManager entityManager) {
        super(ClientRepository.class);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<ClientRepository> searchByCondition(String keyword, Pageable pageable) {
        QClientRepository clientRepository = QClientRepository.clientRepository;
        QClientRepositoryTag clientRepositoryTag = QClientRepositoryTag.clientRepositoryTag;
        QTag tag = QTag.tag;

        // 기본 쿼리 생성
        JPAQuery<ClientRepository> query = queryFactory
                .selectDistinct(clientRepository)
                .from(clientRepository)
                // 태그 검색을 위한 조인
                .leftJoin(clientRepository.clientRepositoryTags, clientRepositoryTag).fetchJoin()
                .leftJoin(clientRepositoryTag.tag, tag).fetchJoin();

        // 동적 검색 조건 생성
        BooleanBuilder builder = new BooleanBuilder();

        // 언어 필터
        if (StringUtils.hasText(keyword)) {
            builder.or(clientRepository.language.equalsIgnoreCase(keyword));
        }

        // 태그 필터
        if (keyword != null && !keyword.isEmpty()) {
            builder.or(clientRepositoryTag.tag.name.in(keyword));
        }

        // 키워드 필터 (이름 또는 설명에 포함)
        if (StringUtils.hasText(keyword)) {
            builder.or(
                    clientRepository.name.containsIgnoreCase(keyword)
                            .or(clientRepository.description.containsIgnoreCase(keyword)));
        }

        // 플랫폼 필터
        if (keyword != null && !keyword.isEmpty()) {
            BooleanExpression platformExpression = null;
            if (platformExpression == null) {
                platformExpression = clientRepository.platforms.contains(keyword);
            } 
            if (platformExpression != null) {
                builder.or(platformExpression);
            }
        }

        // 조건 적용
        if (builder.hasValue()) {
            query.where(builder);
        }

        // 전체 카운트 쿼리
        long total = query.fetchCount();

        // 페이징 및 정렬 적용
        List<ClientRepository> results = getQuerydsl().applyPagination(pageable, query).fetch();

        // 중복 제거 (태그 조인으로 인한 중복 가능성)
        Set<ClientRepository> uniqueResults = new HashSet<>(results);
        List<ClientRepository> finalResults = uniqueResults.stream().toList();

        return new PageImpl<>(finalResults, pageable, total);
    }
}