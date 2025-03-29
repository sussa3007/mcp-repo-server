package com.miraclestudio.mcpreposerver.service.repository.impl;

import com.miraclestudio.mcpreposerver.domain.repository.server.ServerRepository;
import com.miraclestudio.mcpreposerver.domain.repository.server.ServerRepositoryTag;
import com.miraclestudio.mcpreposerver.dto.response.repository.ServerRepositoryResponseDto;
import com.miraclestudio.mcpreposerver.dto.response.repository.ServerRepositorySimpleResponseDto;
import com.miraclestudio.mcpreposerver.exception.BusinessException;
import com.miraclestudio.mcpreposerver.exception.ErrorCode;
import com.miraclestudio.mcpreposerver.repository.server.ServerRepositoryRepository;
import com.miraclestudio.mcpreposerver.service.repository.ServerRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerRepositoryServiceImpl implements ServerRepositoryService {

    private final ServerRepositoryRepository serverRepositoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ServerRepositorySimpleResponseDto> getServerRepositories(String keyword, Pageable pageable) {
        if (StringUtils.hasText(keyword)) {
            return serverRepositoryRepository.findByNameContainingOrDescriptionContaining(keyword, keyword, pageable)
                    .map(ServerRepositorySimpleResponseDto::from);
        }
        return serverRepositoryRepository.findAll(pageable)
                .map(ServerRepositorySimpleResponseDto::from);
    }

    @Override
    @Transactional(readOnly = true)
    public ServerRepositoryResponseDto getServerRepository(Long id) {
        ServerRepository serverRepository = serverRepositoryRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.SERVER_REPOSITORY_NOT_FOUND));

        return ServerRepositoryResponseDto.from(serverRepository);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServerRepositorySimpleResponseDto> searchServerRepositories(String keyword, Pageable pageable) {
        if (!StringUtils.hasText(keyword)) {
            return serverRepositoryRepository.findAll(pageable)
                    .map(ServerRepositorySimpleResponseDto::from);
        }

        return serverRepositoryRepository.searchByCondition(keyword, pageable)
                .map(ServerRepositorySimpleResponseDto::from);
    }

    @Override
    @Transactional(readOnly = true)
    public ServerRepositoryResponseDto getServerRepositoryByOwnerAndRepo(String owner, String repo) {
        ServerRepository serverRepository = serverRepositoryRepository.findByOwnerAndRepo(owner, repo)
                .orElseThrow(() -> new BusinessException(ErrorCode.SERVER_REPOSITORY_NOT_FOUND));

        return ServerRepositoryResponseDto.from(serverRepository);
    }
}