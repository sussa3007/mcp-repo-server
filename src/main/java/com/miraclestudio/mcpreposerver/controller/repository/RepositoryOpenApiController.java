package com.miraclestudio.mcpreposerver.controller.repository;

import com.miraclestudio.mcpreposerver.domain.repository.client.ClientRepository;
import com.miraclestudio.mcpreposerver.domain.repository.server.ServerRepository;
import com.miraclestudio.mcpreposerver.service.repository.ClientRepositoryService;
import com.miraclestudio.mcpreposerver.service.repository.ServerRepositoryService;
import com.miraclestudio.mcpreposerver.dto.response.ApiResponse;
import com.miraclestudio.mcpreposerver.dto.response.repository.ClientRepositorySimpleResponseDto;
import com.miraclestudio.mcpreposerver.dto.response.repository.ClientRepositoryResponseDto;
import com.miraclestudio.mcpreposerver.dto.response.repository.ServerRepositorySimpleResponseDto;
import com.miraclestudio.mcpreposerver.dto.response.repository.ServerRepositoryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/open/repository")
@Tag(name = "Repository API", description = "MCP Repository 조회 관련 API")
public class RepositoryOpenApiController {

    private final ServerRepositoryService serverRepositoryService;
    private final ClientRepositoryService clientRepositoryService;

    /**
     * ServerRepository 목록 조회 (페이징)
     * 
     * @param pageable 페이징 정보
     * @return ServerRepository 목록
     */
    @Operation(summary = "ServerRepository 목록 조회", description = "MCP 서버 레포지토리 목록을 페이징 처리하여 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/servers")
    public ResponseEntity<ApiResponse<Page<ServerRepositorySimpleResponseDto>>> getServers(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Server repositories retrieval request received with page: {}, size: {}, sort: {}", 
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        
        Page<ServerRepositorySimpleResponseDto> servers = serverRepositoryService.searchServerRepositories(keyword, pageable);
        
        return ResponseEntity.ok()
                .body(ApiResponse.success(servers, "Successfully retrieved server repositories."));
    }

    /**
     * ServerRepository 단건 조회
     * 
     * @param id 서버 레포지토리 ID
     * @return ServerRepository 상세 정보
     */
    @Operation(summary = "ServerRepository 상세 조회", description = "특정 MCP 서버 레포지토리의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "레포지토리 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/servers/{id}")
    public ResponseEntity<ApiResponse<ServerRepositoryResponseDto>> getServer(
            @Parameter(description = "서버 레포지토리 ID", required = true) @PathVariable Long id) {
        log.info("Server repository retrieval request received with id: {}", id);
        
        ServerRepositoryResponseDto server = serverRepositoryService.getServerRepository(id);
        
        return ResponseEntity.ok()
                .body(ApiResponse.success(server, "Successfully retrieved server repository."));
    }

    /**
     * ClientRepository 목록 조회 (페이징)
     * 
     * @param pageable 페이징 정보
     * @return ClientRepository 목록
     */
    @Operation(summary = "ClientRepository 목록 조회", description = "MCP 클라이언트 레포지토리 목록을 페이징 처리하여 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/clients")
    public ResponseEntity<ApiResponse<List<ClientRepositorySimpleResponseDto>>> getClients(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Client repositories retrieval request received with page: {}, size: {}, sort: {}", 
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        
        Page<ClientRepositorySimpleResponseDto> clients = clientRepositoryService.searchClientRepositories(keyword, pageable);
        
        return ResponseEntity.ok()
                .body(ApiResponse.success(clients, clients.getContent(), "Successfully retrieved client repositories."));
    }

    /**
     * ClientRepository 단건 조회
     * 
     * @param id 클라이언트 레포지토리 ID
     * @return ClientRepository 상세 정보
     */
    @Operation(summary = "ClientRepository 상세 조회", description = "특정 MCP 클라이언트 레포지토리의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "레포지토리 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/clients/{id}")
    public ResponseEntity<ApiResponse<ClientRepositoryResponseDto>> getClient(
            @Parameter(description = "클라이언트 레포지토리 ID", required = true) @PathVariable Long id) {
        log.info("Client repository retrieval request received with id: {}", id);
        
        ClientRepositoryResponseDto client = clientRepositoryService.getClientRepository(id);
        
        return ResponseEntity.ok()
                .body(ApiResponse.success(client, "Successfully retrieved client repository."));
    }
}

