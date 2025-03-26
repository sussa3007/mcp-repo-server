package com.miraclestudio.mcpreposerver.domain.post;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 10000)
    @Lob
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

    @ElementCollection
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag")
    @Builder.Default
    private Set<String> tags = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "related_posts", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "related_post_id"))
    @Builder.Default
    private List<Post> relatedPosts = new ArrayList<>();

    /**
     * 태그 추가
     */
    public void addTag(String tag) {
        this.tags.add(tag);
    }

    /**
     * 태그 제거
     */
    public void removeTag(String tag) {
        this.tags.remove(tag);
    }

    /**
     * 관련 포스트 추가
     */
    public void addRelatedPost(Post post) {
        if (!this.relatedPosts.contains(post) && !this.equals(post)) {
            this.relatedPosts.add(post);
        }
    }

    /**
     * 관련 포스트 제거
     */
    public void removeRelatedPost(Post post) {
        this.relatedPosts.remove(post);
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