package com.miraclestudio.mcpreposerver.domain.repository.server;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import com.miraclestudio.mcpreposerver.domain.common.Tag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "server_repository_tags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ServerRepositoryTag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serverRepositoryTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_repository_id")
    private ServerRepository serverRepository;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    // 편의 메서드
    public void setServerRepository(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}