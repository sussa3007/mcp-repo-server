package com.miraclestudio.mcpreposerver.domain.repository.server;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mcp_tools")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class McpTool extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mcpToolId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 20)
    private String version;

    @Column(columnDefinition = "TEXT")
    private String usageInfo;

    @Column(columnDefinition = "TEXT")
    private String documentation;

    @Column(nullable = false)
    private Boolean isRequired;

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