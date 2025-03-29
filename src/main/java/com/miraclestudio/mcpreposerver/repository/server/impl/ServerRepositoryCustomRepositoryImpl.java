package com.miraclestudio.mcpreposerver.repository.server.impl;

import com.miraclestudio.mcpreposerver.domain.repository.server.ServerRepository;
import com.miraclestudio.mcpreposerver.domain.repository.server.QServerRepository;
import com.miraclestudio.mcpreposerver.domain.repository.server.QServerRepositoryTag;
import com.miraclestudio.mcpreposerver.domain.common.QTag;
import com.miraclestudio.mcpreposerver.repository.server.ServerRepositoryCustomRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
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
public class ServerRepositoryCustomRepositoryImpl extends QuerydslRepositorySupport
        implements ServerRepositoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public ServerRepositoryCustomRepositoryImpl(EntityManager entityManager) {
        super(ServerRepository.class);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<ServerRepository> searchByCondition(String keyword, Pageable pageable) {
        QServerRepository serverRepository = QServerRepository.serverRepository;
        QServerRepositoryTag serverRepositoryTag = QServerRepositoryTag.serverRepositoryTag;
        QTag tag = QTag.tag;

        // 기본 쿼리 생성
        JPAQuery<ServerRepository> query = queryFactory
                .selectDistinct(serverRepository)
                .from(serverRepository)
                // 태그 검색을 위한 조인
                .leftJoin(serverRepository.serverRepositoryTags, serverRepositoryTag).fetchJoin()
                .leftJoin(serverRepositoryTag.tag, tag).fetchJoin();

        // 동적 검색 조건 생성
        BooleanBuilder builder = new BooleanBuilder();

        // 언어 필터
        if (StringUtils.hasText(keyword)) {
            builder.or(serverRepository.language.equalsIgnoreCase(keyword));
        }

        // 태그 필터
        if (StringUtils.hasText(keyword)) {
            builder.or(serverRepositoryTag.tag.name.containsIgnoreCase(keyword));
        }

        // 키워드 필터 (이름 또는 설명에 포함)
        if (StringUtils.hasText(keyword)) {
            builder.or(
                    serverRepository.name.containsIgnoreCase(keyword)
                            .or(serverRepository.description.containsIgnoreCase(keyword)));
        }

        // 조건 적용
        if (builder.hasValue()) {
            query.where(builder);
        }

        // 전체 카운트 쿼리
        long total = query.fetchCount();

        // 페이징 및 정렬 적용
        List<ServerRepository> results = getQuerydsl().applyPagination(pageable, query).fetch();

        // 중복 제거 (태그 조인으로 인한 중복 가능성)
        Set<ServerRepository> uniqueResults = new HashSet<>(results);
        List<ServerRepository> finalResults = uniqueResults.stream().toList();

        return new PageImpl<>(finalResults, pageable, total);
    }
}