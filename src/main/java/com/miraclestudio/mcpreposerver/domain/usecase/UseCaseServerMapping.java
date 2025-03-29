package com.miraclestudio.mcpreposerver.domain.usecase;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import com.miraclestudio.mcpreposerver.domain.repository.server.ServerRepository;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "use_case_server_repositories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UseCaseServerMapping extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long useCaseServerMappingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "use_case_id")
    private UseCase useCase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_repository_id")
    private ServerRepository serverRepository;

    public void setUseCase(UseCase useCase) {
        this.useCase = useCase;
    }

    public void setServerRepository(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }
}