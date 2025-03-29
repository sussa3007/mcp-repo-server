package com.miraclestudio.mcpreposerver.security.oauth;

import com.miraclestudio.mcpreposerver.domain.user.Role;
import com.miraclestudio.mcpreposerver.domain.user.SocialType;
import com.miraclestudio.mcpreposerver.domain.user.User;
import com.miraclestudio.mcpreposerver.exception.ErrorCode;
import com.miraclestudio.mcpreposerver.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.oauth2.core.OAuth2Error;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oauth2User.getAttributes();

        User user = saveOrUpdate(socialType, attributes);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())),
                attributes,
                userNameAttributeName);
    }

    private SocialType getSocialType(String registrationId) {
        return switch (registrationId.toLowerCase()) {
            case "github" -> SocialType.GITHUB;
            case "google" -> SocialType.GOOGLE;
            default -> throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다.");
        };
    }

    @Transactional
    protected User saveOrUpdate(SocialType socialType, Map<String, Object> attributes) {
        String email = getEmail(socialType, attributes);
        String name = getName(socialType, attributes);
        String imageUrl = getImageUrl(socialType, attributes);
        String socialId = getSocialId(socialType, attributes);

        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(name, imageUrl))
                .orElse(User.builder()
                        .email(email)
                        .name(name)
                        .imageUrl(imageUrl)
                        .socialType(socialType)
                        .socialId(socialId)
                        .role(Role.USER)
                        .build()
                        );
        if (user.getSocialType().equals(socialType)) {
            return userRepository.save(user);
        } else {
            throw new OAuth2AuthenticationException(new OAuth2Error("USER_ALREADY_EXISTS"),
                    user.getSocialType().toString());
        }

    }

    private String getEmail(SocialType socialType, Map<String, Object> attributes) {
        return switch (socialType) {
            case GITHUB -> (String) attributes.get("email");
            case GOOGLE -> (String) attributes.get("email");
        };
    }

    private String getName(SocialType socialType, Map<String, Object> attributes) {
        return switch (socialType) {
            case GITHUB -> (String) attributes.get("name");
            case GOOGLE -> (String) attributes.get("name");
        };
    }

    private String getImageUrl(SocialType socialType, Map<String, Object> attributes) {
        return switch (socialType) {
            case GITHUB -> (String) attributes.get("avatar_url");
            case GOOGLE -> (String) attributes.get("picture");
        };
    }

    private String getSocialId(SocialType socialType, Map<String, Object> attributes) {
        return switch (socialType) {
            case GITHUB -> String.valueOf(attributes.get("id"));
            case GOOGLE -> (String) attributes.get("sub");
        };
    }
}