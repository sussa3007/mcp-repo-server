package com.miraclestudio.mcpreposerver.repository.user;

import com.miraclestudio.mcpreposerver.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndSocialId(String email, String socialId);


    Optional<User> findBySocialTypeAndSocialId(String socialType, String socialId);

    boolean existsByEmail(String email);

    Optional<User> findBySocialId(String so);
}