package com.miraclestudio.mcpreposerver.domain.user;

import com.miraclestudio.mcpreposerver.domain.common.BaseTimeEntity;
import com.miraclestudio.mcpreposerver.domain.submission.Submission;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;

    @Column(nullable = false, unique = true)
    private String socialId;

    @ElementCollection
    private Set<String> favorites = new LinkedHashSet();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Submission> submissions = new LinkedHashSet();

    public User update(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        return this;
    }

    public void addFavorite(String favoriteId) {
        this.favorites.add(favoriteId);
    }

    public void removeFavorite(String favoriteId) {
        this.favorites.remove(favoriteId);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void promoteToAdmin() {
        this.role = Role.ADMIN;
    }

    public void addSubmission(Submission submission) {
        this.submissions.add(submission);
        submission.setUser(this);
    }

    /**
     * 비밀번호 변경
     */
    public void updatePassword(String newPassword) {
        // Implementation needed
    }

    /**
     * 프로필 정보 업데이트
     */
    public void updateProfile(String name, String avatarUrl) {
        this.name = name;
        this.imageUrl = avatarUrl;
    }
}