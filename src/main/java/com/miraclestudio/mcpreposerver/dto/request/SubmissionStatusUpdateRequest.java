package com.miraclestudio.mcpreposerver.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(description = "제출 상태 업데이트 요청")
public class SubmissionStatusUpdateRequest {

    @NotBlank(message = "상태는 필수입니다.")
    @Pattern(regexp = "^(APPROVED|REJECTED)$", message = "상태는 APPROVED 또는 REJECTED만 가능합니다.")
    @Schema(description = "새로운 상태 (APPROVED/REJECTED)", example = "APPROVED")
    private String status;

    @NotBlank(message = "메시지는 필수입니다.")
    @Schema(description = "상태 변경 메시지", example = "프로젝트가 승인되었습니다.")
    private String message;
}