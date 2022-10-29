package com.zzangmin.gesipan.layer.basiccrud.entity;

import javax.persistence.*;

import com.zzangmin.gesipan.layer.login.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime updatedAt;

    public void update(String commentContent, LocalDateTime updatedAt) {
        this.commentContent = commentContent;
        this.updatedAt = updatedAt;
    }

}
