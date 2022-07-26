package com.zzangmin.gesipan.web.dto.comment;

import com.zzangmin.gesipan.web.entity.Comment;
import com.zzangmin.gesipan.web.entity.Post;
import com.zzangmin.gesipan.web.entity.Users;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
public class CommentSaveRequest {

    @NotBlank
    @Positive
    private Long referencePostId;
    @NotBlank
    @Positive
    private Long userId;
    @NotBlank
    private String commentContent;
    @NotBlank
    @PastOrPresent
    private LocalDateTime createdAt;

    public Comment toEntity(Users user, Post post) {
        return Comment.builder()
                .commentContent(this.commentContent)
                .createdAt(this.createdAt)
                .updatedAt(this.createdAt)
                .user(user)
                .post(post)
                .build();
    }
}
