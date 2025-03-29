package com.miraclestudio.mcpreposerver.repository.common;

import com.miraclestudio.mcpreposerver.domain.common.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}