package com.miraclestudio.mcpreposerver.dto.response.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.miraclestudio.mcpreposerver.domain.repository.server.ServerRepository;
import com.miraclestudio.mcpreposerver.domain.repository.server.ServerRepositoryTag;
import com.miraclestudio.mcpreposerver.domain.common.Tag;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerRepositorySimpleResponseDto {
    private Long id;
    private String name;
    private String description;
    private String owner;
    private String repo;
    private String githubUrl;
    private String demoUrl;
    private Boolean isOfficial;
    private String language;
    private Integer stars;
    private Integer forks;
    private String license;
    private List<String> tags;

    public static ServerRepositorySimpleResponseDto from(ServerRepository serverRepository) {
        return ServerRepositorySimpleResponseDto.builder()
                .id(serverRepository.getServerRepositoryId())
                .name(serverRepository.getName())
                .description(serverRepository.getDescription())
                .owner(serverRepository.getOwner())
                .repo(serverRepository.getRepo())
                .githubUrl(serverRepository.getGithubUrl())
                .demoUrl(serverRepository.getDemoUrl())
                .isOfficial(serverRepository.getIsOfficial())
                .language(serverRepository.getLanguage())
                .stars(serverRepository.getStars())
                .forks(serverRepository.getForks())
                .license(serverRepository.getLicense())
                .tags(serverRepository.getServerRepositoryTags().stream()
                        .map(ServerRepositoryTag::getTag)
                        .map(Tag::getName)
                        .collect(Collectors.toList()))
                .build();
    }
}
