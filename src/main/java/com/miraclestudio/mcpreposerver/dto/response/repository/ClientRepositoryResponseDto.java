package com.miraclestudio.mcpreposerver.dto.response.repository;

import com.miraclestudio.mcpreposerver.domain.repository.client.ClientRepository;
import com.miraclestudio.mcpreposerver.domain.repository.client.ClientRepositoryTag;
import com.miraclestudio.mcpreposerver.domain.common.Tag;
import com.miraclestudio.mcpreposerver.domain.repository.client.ClientContributor;
import com.miraclestudio.mcpreposerver.domain.repository.client.ClientContributorMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientRepositoryResponseDto {
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
    private String installInstructions;
    private Set<String> usageExamples;
    private Set<String> supportedLanguages;
    private Set<String> platforms;
    private List<ContributorDto> contributors;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * ContributorDto 내부 클래스
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ContributorDto {
        private String name;
        private String githubUrl;
        private String avatarUrl;

        /**
         * Contributor 엔티티로부터 DTO를 생성합니다.
         *
         * @param contributor Contributor 엔티티
         * @return ContributorDto 객체
         */
        public static ContributorDto from(ClientContributor contributor) {
            return ContributorDto.builder()
                    .name(contributor.getName())
                    .githubUrl(contributor.getGithubUrl())
                    .avatarUrl(contributor.getAvatarUrl())
                    .build();
        }
    }

    /**
     * ClientRepository 엔티티로부터 DTO를 생성합니다.
     *
     * @param clientRepository ClientRepository 엔티티
     * @return ClientRepositoryResponseDto 객체
     */
    public static ClientRepositoryResponseDto from(ClientRepository clientRepository) {
        return ClientRepositoryResponseDto.builder()
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
                        .collect(Collectors.toList()))
                .installInstructions(clientRepository.getInstallInstructions())
                .usageExamples(clientRepository.getUsageExamples())
                .supportedLanguages(clientRepository.getSupportedLanguages())
                .platforms(clientRepository.getPlatforms())
                .contributors(clientRepository.getContributorMappings().stream()
                        .map(ClientContributorMapping::getContributor)
                        .map(ContributorDto::from)
                        .collect(Collectors.toList()))
                .createdAt(clientRepository.getCreatedAt())
                .updatedAt(clientRepository.getUpdatedAt())
                .build();
    }
}

