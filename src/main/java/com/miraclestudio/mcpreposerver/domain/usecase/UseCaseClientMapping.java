package com.miraclestudio.mcpreposerver.domain.usecase;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import com.miraclestudio.mcpreposerver.domain.repository.client.ClientRepository;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "use_case_client_repositories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UseCaseClientMapping extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long useCaseClientMappingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "use_case_id")
    private UseCase useCase;    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_repositories_id")
    private ClientRepository clientRepository;

    public void setUseCase(UseCase useCase) {
        this.useCase = useCase;
    }

    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
}