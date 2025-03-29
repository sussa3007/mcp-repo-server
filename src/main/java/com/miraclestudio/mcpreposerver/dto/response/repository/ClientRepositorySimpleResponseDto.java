package com.miraclestudio.mcpreposerver.dto.response.repository;

import com.miraclestudio.mcpreposerver.domain.repository.client.ClientRepository;
import com.miraclestudio.mcpreposerver.domain.repository.client.ClientRepositoryTag;
import com.miraclestudio.mcpreposerver.domain.common.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientRepositorySimpleResponseDto {
    private Long id;
    private String name;
    private String description;
    private String owner;
    private String repo;
    private String githubUrl;
    private Boolean isOfficial;
    private String language;
    private Integer stars;
    private Integer forks;
    private String license;
    private List<String> tags;
    public static ClientRepositorySimpleResponseDto from(ClientRepository clientRepository) {
        return ClientRepositorySimpleResponseDto.builder()
                .id(clientRepository.getClientRepositoryId())
                .name(clientRepository.getName())
                .description(clientRepository.getDescription())
                .owner(clientRepository.getOwner())
                .repo(clientRepository.getRepo())
                .githubUrl(clientRepository.getGithubUrl())
                .isOfficial(clientRepository.getIsOfficial())
                .language(clientRepository.getLanguage())
                .stars(clientRepository.getStars())
                .forks(clientRepository.getForks())
                .license(clientRepository.getLicense())
                .tags(clientRepository.getClientRepositoryTags().stream()
                        .map(ClientRepositoryTag::getTag)
                        .map(Tag::getName)
                        .toList())
                .build();
    }
}
