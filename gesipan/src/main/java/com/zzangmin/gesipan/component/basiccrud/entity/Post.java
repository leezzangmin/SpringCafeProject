package com.zzangmin.gesipan.component.basiccrud.entity;

import javax.persistence.*;

import com.zzangmin.gesipan.component.embeddable.BaseTime;
import com.zzangmin.gesipan.component.login.entity.Users;
import lombok.*;

import java.time.LocalDateTime;


@ToString(exclude = {"postCategory", "user"})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    @Column(nullable = false, length = 1000)
    private String postSubject;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String postContent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_category_id")
    private PostCategory postCategory;
    @Column(nullable = true, columnDefinition = "BIGINT default 0")
    private Long hitCount;
    @Embedded
    private BaseTime baseTime;

    public LocalDateTime getCreatedAt() {
        return baseTime.getCreatedAt();
    }

    public LocalDateTime getUpdatedAt() {
        return baseTime.getUpdatedAt();
    }

    public void update(String postSubject, String postContent, LocalDateTime updateAt) {
        this.postSubject = postSubject;
        this.postContent = postContent;
        this.baseTime.refreshUpdatedAt(updateAt);
    }


}
