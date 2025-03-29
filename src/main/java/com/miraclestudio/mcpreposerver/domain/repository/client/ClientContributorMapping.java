package com.miraclestudio.mcpreposerver.domain.repository.client;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "client_repository_contributors")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ClientContributorMapping extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientContributorMappingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_repository_id")
    private ClientRepository clientRepository;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contributor_id")
    private ClientContributor contributor;

    // 추가적인 매핑 정보가 필요할 경우 여기에 필드 추가
    // 예: 기여도, 역할 등

    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void setContributor(ClientContributor contributor) {
        this.contributor = contributor;
    }


}