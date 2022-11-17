package com.zzangmin.gesipan.component.basiccrud.entity;

import javax.persistence.*;

import com.zzangmin.gesipan.component.embeddable.BaseTime;
import com.zzangmin.gesipan.component.login.entity.Users;
import lombok.*;

import java.time.LocalDateTime;

@ToString(exclude = {"post", "user"})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    @Column(nullable = false, length = 1000)
    private String commentContent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_post_id")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;
    @Embedded
    private BaseTime baseTime;

    public LocalDateTime getCreatedAt() {
        return baseTime.getCreatedAt();
    }

    public LocalDateTime getUpdatedAt() {
        return baseTime.getUpdatedAt();
    }

    public void update(String commentContent, LocalDateTime updatedAt) {
        this.commentContent = commentContent;
        this.baseTime.refreshUpdatedAt(updatedAt);
    }

}
