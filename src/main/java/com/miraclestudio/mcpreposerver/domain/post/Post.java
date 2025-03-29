package com.miraclestudio.mcpreposerver.domain.post;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import com.miraclestudio.mcpreposerver.domain.common.Tag;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 500)
    private String summary;

    private String imageUrl;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private LocalDateTime publishedAt;

    @Column(nullable = false)
    private String author;

    private String authorAvatarUrl;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<PostTag> postTags = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RelatedPostMapping> relatedPostMappings = new LinkedList<>();

    /**
     * 태그 추가
     */
    public void addPostTag(PostTag tag) {
        this.postTags.add(tag);
        tag.setPost(this);
    }

    /**
     * 태그 제거
     */
    public void removePostTag(PostTag tag) {
        this.postTags.removeIf(postTag -> postTag.getTag().equals(tag));
    }

    /**
     * 태그 목록 조회
     */
    public List<String> getTagNames() {
        return this.postTags.stream()
                .map(PostTag::getTag)
                .map(Tag::getName)
                .toList();
    }

    /**
     * 관련 포스트 추가
     */
    public void addRelatedPost(Post relatedPost) {
        if (!this.equals(relatedPost)) {
            RelatedPostMapping mapping = RelatedPostMapping.builder()
                    .post(this)
                    .relatedPost(relatedPost)
                    .build();
            this.relatedPostMappings.add(mapping);
            mapping.setPost(this);
            mapping.setRelatedPost(relatedPost);
        }
    }

    /**
     * 관련 포스트 제거
     */
    public void removeRelatedPost(Post relatedPost) {
        this.relatedPostMappings.removeIf(mapping -> mapping.getRelatedPost().equals(relatedPost));
    }

    /**
     * 포스트 내용 업데이트
     */
    public void updateContent(String title, String content, String summary, String imageUrl, String category) {
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.imageUrl = imageUrl;
        this.category = category;
    }
}