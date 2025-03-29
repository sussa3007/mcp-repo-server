package com.miraclestudio.mcpreposerver.domain.submission;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import com.miraclestudio.mcpreposerver.domain.common.Tag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "submission_tags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SubmissionTag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private Submission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    // 편의 메서드
    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}