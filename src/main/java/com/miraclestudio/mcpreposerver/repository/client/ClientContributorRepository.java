package com.miraclestudio.mcpreposerver.repository.client;

import org.springframework.data.jpa.repository.JpaRepository;
import com.miraclestudio.mcpreposerver.domain.repository.client.ClientContributor;

public interface ClientContributorRepository extends JpaRepository<ClientContributor, Long> {
    // 기본 CRUD 작업은 JpaRepository에서 제공
}