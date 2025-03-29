package com.miraclestudio.mcpreposerver.domain.repository.client;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import com.miraclestudio.mcpreposerver.domain.common.Tag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "client_repository_tag_mappings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ClientRepositoryTag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_repository_id", nullable = false)
    private ClientRepository clientRepository;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    // 편의 메서드
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}