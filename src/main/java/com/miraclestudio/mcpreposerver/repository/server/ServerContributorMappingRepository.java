package com.miraclestudio.mcpreposerver.repository.server;

import org.springframework.data.jpa.repository.JpaRepository;
import com.miraclestudio.mcpreposerver.domain.repository.server.ServerContributorMapping;

public interface ServerContributorMappingRepository extends JpaRepository<ServerContributorMapping, Long> {
    // 기본 CRUD 작업은 JpaRepository에서 제공
}