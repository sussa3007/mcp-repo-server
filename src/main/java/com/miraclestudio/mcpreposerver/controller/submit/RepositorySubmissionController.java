package com.miraclestudio.mcpreposerver.controller.submit;

import com.miraclestudio.mcpreposerver.domain.submission.Submission.RepositoryType;
import com.miraclestudio.mcpreposerver.domain.submission.Submission.Status;
import com.miraclestudio.mcpreposerver.dto.request.SubmissionRequest;
import com.miraclestudio.mcpreposerver.dto.request.SubmissionStatusUpdateRequest;
import com.miraclestudio.mcpreposerver.dto.response.ApiResponse;
import com.miraclestudio.mcpreposerver.dto.response.SubmissionResponse;
import com.miraclestudio.mcpreposerver.security.jwt.JwtTokenProvider.CustomUserDetails;
import com.miraclestudio.mcpreposerver.service.submission.SubmissionService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/submissions")
@Tag(name = "Repository Submission", description = "레포지토리 제출 관련 API")
public class RepositorySubmissionController {

    private final SubmissionService submissionService;

    @Operation(summary = "프로젝트 제출", description = "새로운 MCP 서버 또는 클라이언트 프로젝트를 제출합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "제출 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<SubmissionResponse>> submitRepository(
            @Valid @RequestBody SubmissionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        log.info("Repository submission request received: {}", request);

        SubmissionResponse response = submissionService.submitRepository(
                userDetails.getEmail(),
                request.getName(),
                request.getAuthor(),
                RepositoryType.valueOf(request.getType().toUpperCase()),
                request.getDescription(),
        request.getRepoUrl(),
                request.getWebsiteUrl(),
                request.getTags(),
                request.getEmail()
        );

 

        return ResponseEntity.status(201)
                .body(ApiResponse.success(response, "Successfully submitted project."));
    }

    @Operation(summary = "프로젝트 재분석 요청", description = "제출된 프로젝트를 재분석합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "제출 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/reanalyze/{id}")
    public ResponseEntity<ApiResponse<SubmissionResponse>> reanalyzeRepository(
            @Valid @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        log.info("Repository reanalysis request received: {}", id);
        
        SubmissionResponse response = submissionService.reanalyzeRepository(
                userDetails.getEmail(),
                id
        );

 

        return ResponseEntity.status(201)
                .body(ApiResponse.success(response, "Successfully submitted project."));
    }

    @Operation(summary = "제출 상태 조회", description = "제출한 프로젝트의 검토 상태를 확인합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "제출 정보 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubmissionResponse>> getSubmissionStatus(
            @Parameter(description = "제출 ID", required = true) @PathVariable Long id) {
        log.info("Submission status check request received: id={}", id);

        SubmissionResponse response = submissionService.getSubmission(id);

        return ResponseEntity.ok()
                .body(ApiResponse.success(response, "Successfully retrieved submission status."));
    }

    @Operation(summary = "제출 상태 업데이트", description = "관리자가 제출된 프로젝트의 상태를 수동으로 업데이트합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "업데이트 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "제출 정보 없음")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<SubmissionResponse>> updateSubmissionStatus(
            @Parameter(description = "제출 ID", required = true) @PathVariable Long id,
            @Valid @RequestBody SubmissionStatusUpdateRequest request) {
        log.info("Submission status update request received: id={}, status={}", id, request.getStatus());

        SubmissionResponse response = submissionService.updateSubmissionStatus(
                id,
                Status.valueOf(request.getStatus().toUpperCase()),
                request.getMessage());

        return ResponseEntity.ok()
                .body(ApiResponse.success(response, "Successfully updated submission status."));
    }

    @Operation(summary = "회원의 모든 제출 목록 조회", description = "회원의 모든 제출 목록을 페이징 처리하여 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패")
    })

    @GetMapping
    public ResponseEntity<ApiResponse<List<SubmissionResponse>>> getAllSubmissions(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("All submissions retrieval request received with page: {}, size: {}, sort: {}", 
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<SubmissionResponse> responses = submissionService.getUserSubmissions(userDetails.getEmail(), pageable);

        return ResponseEntity.ok()
                .body(ApiResponse.success(responses, responses.getContent(), "Successfully retrieved all submissions."));
    }
}
