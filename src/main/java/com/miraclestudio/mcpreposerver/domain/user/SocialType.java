package com.miraclestudio.mcpreposerver.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialType {
    GITHUB("github"),
    GOOGLE("google");

    private final String registrationId;
}