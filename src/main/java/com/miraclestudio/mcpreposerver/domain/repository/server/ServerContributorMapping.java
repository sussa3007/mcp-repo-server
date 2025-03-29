package com.miraclestudio.mcpreposerver.domain.repository.server;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "server_repository_contributors")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ServerContributorMapping extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serverContributorMappingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_repository_id")
    private ServerRepository serverRepository;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contributor_id")
    private Contributor contributor;

    // 추가적인 매핑 정보가 필요할 경우 여기에 필드 추가
    // 예: 기여도, 역할 등

    public void setServerRepository(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public void setContributor(Contributor contributor) {
        this.contributor = contributor;
    }
}