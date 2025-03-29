package com.miraclestudio.mcpreposerver.domain.repository.server;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "commands")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Command extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commandId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String command;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 20)
    private String type;

    @Column(length = 50)
    private String platform;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_repository_id")
    private ServerRepository serverRepository;

    /**
     * 서버 레포지토리 설정
     */
    public void setServerRepository(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }
}