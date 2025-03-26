package com.miraclestudio.mcpreposerver.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 공통 에러 코드 (1000번대)
    INVALID_INPUT_VALUE(1000, "유효하지 않은 입력값입니다."),
    METHOD_NOT_ALLOWED(1001, "허용되지 않은 메소드입니다."),
    ENTITY_NOT_FOUND(1002, "데이터를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(1003, "서버 오류가 발생했습니다."),
    INVALID_TYPE_VALUE(1004, "유효하지 않은 타입입니다."),

    // 인증/인가 관련 에러 (2000번대)
    UNAUTHORIZED(2000, "인증이 필요합니다."),
    ACCESS_DENIED(2001, "접근 권한이 없습니다."),
    INVALID_TOKEN(2002, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(2003, "만료된 토큰입니다."),

    // 사용자 관련 에러 (3000번대)
    USER_NOT_FOUND(3000, "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(3001, "이미 사용 중인 이메일입니다."),
    DUPLICATE_USERNAME(3002, "이미 사용 중인 사용자명입니다."),
    WRONG_PASSWORD(3003, "잘못된 비밀번호입니다."),

    // GitHub API 관련 에러 (4000번대)
    GITHUB_API_ERROR(4000, "GitHub API 호출 중 오류가 발생했습니다."),
    GITHUB_REPO_NOT_FOUND(4001, "GitHub 레포지토리를 찾을 수 없습니다."),
    GITHUB_RATE_LIMIT_EXCEEDED(4002, "GitHub API 요청 한도를 초과했습니다."),
    GITHUB_INVALID_TOKEN(4003, "유효하지 않은 GitHub 토큰입니다."),

    // 레포지토리 관련 에러 (5000번대)
    SERVER_REPO_NOT_FOUND(5000, "서버 레포지토리를 찾을 수 없습니다."),
    CLIENT_REPO_NOT_FOUND(5001, "클라이언트 레포지토리를 찾을 수 없습니다."),
    API_ENDPOINT_NOT_FOUND(5002, "API 엔드포인트를 찾을 수 없습니다."),
    ENVIRONMENT_VARIABLE_NOT_FOUND(5003, "환경 변수를 찾을 수 없습니다."),
    COMMAND_NOT_FOUND(5004, "명령어를 찾을 수 없습니다."),
    DEPLOYMENT_OPTION_NOT_FOUND(5005, "배포 옵션을 찾을 수 없습니다."),
    CONTRIBUTOR_NOT_FOUND(5006, "기여자를 찾을 수 없습니다."),

    // 제출 관련 에러 (6000번대)
    SUBMISSION_NOT_FOUND(6000, "제출 정보를 찾을 수 없습니다."),
    INVALID_SUBMISSION_STATUS(6001, "유효하지 않은 제출 상태입니다."),
    SUBMISSION_ALREADY_PROCESSED(6002, "이미 처리된 제출입니다."),
    GITHUB_URL_INVALID(6003, "유효하지 않은 GitHub URL입니다.");

    private final int code;
    private final String message;
}