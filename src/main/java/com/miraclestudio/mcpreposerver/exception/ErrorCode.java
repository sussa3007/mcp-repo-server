package com.miraclestudio.mcpreposerver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 BAD_REQUEST
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "E400-001", "입력값이 유효하지 않습니다."),
    GITHUB_URL_INVALID(HttpStatus.BAD_REQUEST, "E400-002", "유효하지 않은 GitHub URL입니다."),
    INVALID_REPOSITORY_TYPE(HttpStatus.BAD_REQUEST, "E400-003", "유효하지 않은 레포지토리 유형입니다."),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "E400-004", "유효하지 않은 이메일 형식입니다."),
    MANDATORY_FIELD_MISSING(HttpStatus.BAD_REQUEST, "E400-005", "필수 항목이 누락되었습니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "E400-006", "유효하지 않은 타입입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "E405-001", "지원하지 않는 HTTP 메서드입니다."),

    // 401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "E401-001", "인증되지 않은 요청입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "E401-002", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "E401-003", "만료된 토큰입니다."),
    GITHUB_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "E401-004", "유효하지 않은 GitHub 토큰입니다."),

    // 403 FORBIDDEN
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "E403-001", "접근 권한이 없습니다."),

    // 404 NOT_FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "E404-001", "사용자를 찾을 수 없습니다."),
    SUBMISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "E404-002", "제출 정보를 찾을 수 없습니다."),
    SERVER_REPOSITORY_NOT_FOUND(HttpStatus.NOT_FOUND, "E404-003", "서버 레포지토리를 찾을 수 없습니다."),
    CLIENT_REPOSITORY_NOT_FOUND(HttpStatus.NOT_FOUND, "E404-004", "클라이언트 레포지토리를 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "E404-005", "포스트를 찾을 수 없습니다."),
    USE_CASE_NOT_FOUND(HttpStatus.NOT_FOUND, "E404-006", "유스케이스를 찾을 수 없습니다."),
    GITHUB_REPOSITORY_NOT_FOUND(HttpStatus.NOT_FOUND, "E404-007", "GitHub 레포지토리를 찾을 수 없습니다."),
    GITHUB_REPO_NOT_FOUND(HttpStatus.NOT_FOUND, "E404-008", "GitHub 레포지토리를 찾을 수 없습니다."),
    GITHUB_README_NOT_FOUND(HttpStatus.NOT_FOUND, "E404-009", "GitHub README를 찾을 수 없습니다."),

    // 409 CONFLICT
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "E409-001", "이미 존재하는 리소스입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "E409-002", "이미 사용 중인 이메일입니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "E409-003", "이미 사용 중인 사용자 이름입니다."),
    DUPLICATE_SUBMISSION(HttpStatus.CONFLICT, "E409-004", "이미 제출된 레포지토리입니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "E409-005", "이미 존재하는 사용자입니다."),

    // 422 UNPROCESSABLE_ENTITY
    SUBMISSION_ALREADY_PROCESSED(HttpStatus.UNPROCESSABLE_ENTITY, "E422-001", "이미 처리된 제출입니다."),
    GITHUB_API_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, "E422-002", "GitHub API 오류가 발생했습니다."),
    GITHUB_RATE_LIMIT_EXCEEDED(HttpStatus.UNPROCESSABLE_ENTITY, "E422-003", "GitHub API 요청 제한을 초과했습니다."),

    // 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E500-001", "서버 내부 오류가 발생했습니다."),
    GITHUB_PARSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E500-002", "GitHub 데이터 파싱 중 오류가 발생했습니다."),
    AI_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E500-003", "AI 서비스 연동 중 오류가 발생했습니다."),
    SUBMISSION_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "E500-005", "제출 상태 업데이트 실패"),

    // Contributor 관련 에러 (5000번대)
    CONTRIBUTOR_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E500-004", "컨트리뷰터 저장 중 오류가 발생했습니다."),

    // 시스템 에러 (9000번대)
    INTERNAL_SERVER_ERROR_9000(HttpStatus.INTERNAL_SERVER_ERROR, "E900-001", "내부 서버 오류가 발생했습니다."),

    // OAuth2-related errors
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "AUTH_005", "잘못된 요청입니다."),
    UNAUTHORIZED_CLIENT(HttpStatus.UNAUTHORIZED, "AUTH_006", "인증되지 않은 클라이언트입니다."),
    ACCESS_DENIED_OAUTH2(HttpStatus.FORBIDDEN, "AUTH_007", "접근이 거부되었습니다."),
    OAUTH2_AUTHENTICATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_008", "OAuth2 인증 중 오류가 발생했습니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTH_009", "인증에 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    public int getCode() {
        return Integer.parseInt(this.code.replace("E", "").substring(0, 3));
    }

    public String getStringErrorCode() {
        return this.code;
    }
}