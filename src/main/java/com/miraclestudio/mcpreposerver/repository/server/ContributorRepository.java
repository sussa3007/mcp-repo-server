package com.miraclestudio.mcpreposerver.repository.server;

import org.springframework.data.jpa.repository.JpaRepository;
import com.miraclestudio.mcpreposerver.domain.repository.server.Contributor;

public interface ContributorRepository extends JpaRepository<Contributor, Long> {
    // 기본 CRUD 작업은 JpaRepository에서 제공
}