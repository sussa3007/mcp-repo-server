package com.miraclestudio.mcpreposerver.service.repository.impl;

import com.miraclestudio.mcpreposerver.domain.repository.client.ClientRepository;
import com.miraclestudio.mcpreposerver.dto.response.repository.ClientRepositoryResponseDto;
import com.miraclestudio.mcpreposerver.dto.response.repository.ClientRepositorySimpleResponseDto;
import com.miraclestudio.mcpreposerver.exception.BusinessException;
import com.miraclestudio.mcpreposerver.exception.ErrorCode;
import com.miraclestudio.mcpreposerver.repository.client.ClientRepositoryRepository;
import com.miraclestudio.mcpreposerver.service.repository.ClientRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientRepositoryServiceImpl implements ClientRepositoryService {

    private final ClientRepositoryRepository clientRepositoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ClientRepositorySimpleResponseDto> getClientRepositories(String keyword, Pageable pageable) {
        if (StringUtils.hasText(keyword)) {
            return clientRepositoryRepository.findByNameContainingOrDescriptionContaining(keyword, keyword, pageable)
                    .map(ClientRepositorySimpleResponseDto::from);
        }
        return clientRepositoryRepository.findAll(pageable)
                .map(ClientRepositorySimpleResponseDto::from);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientRepositoryResponseDto getClientRepository(Long id) {
        ClientRepository clientRepository = clientRepositoryRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLIENT_REPOSITORY_NOT_FOUND));

        return ClientRepositoryResponseDto.from(clientRepository);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientRepositorySimpleResponseDto> searchClientRepositories(String keyword, Pageable pageable) {
        // 검색 조건이 없을 경우 전체 조회
        if (!StringUtils.hasText(keyword)) {
            return clientRepositoryRepository.findAll(pageable)
                    .map(ClientRepositorySimpleResponseDto::from);
        }

        // 검색 조건에 따른 쿼리 수행 (단일 쿼리로 최적화)
        return clientRepositoryRepository.searchByCondition(keyword, pageable)
                .map(ClientRepositorySimpleResponseDto::from);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientRepositoryResponseDto getClientRepositoryByOwnerAndRepo(String owner, String repo) {
        ClientRepository clientRepository = clientRepositoryRepository.findByOwnerAndRepo(owner, repo)
                .orElseThrow(() -> new BusinessException(ErrorCode.CLIENT_REPOSITORY_NOT_FOUND));

        return ClientRepositoryResponseDto.from(clientRepository);
    }
}