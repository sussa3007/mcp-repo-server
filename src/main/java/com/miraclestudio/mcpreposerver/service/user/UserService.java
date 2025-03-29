package com.miraclestudio.mcpreposerver.service.user;

import com.miraclestudio.mcpreposerver.repository.user.UserRepository;
import com.miraclestudio.mcpreposerver.exception.BusinessException;
import com.miraclestudio.mcpreposerver.exception.ErrorCode;
import com.miraclestudio.mcpreposerver.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserResponse findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserResponse::from)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
