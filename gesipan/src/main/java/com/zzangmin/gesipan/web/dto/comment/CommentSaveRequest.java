package com.zzangmin.gesipan.web.dto.comment;

import com.zzangmin.gesipan.web.entity.Comment;
import com.zzangmin.gesipan.web.entity.Post;
import com.zzangmin.gesipan.web.entity.Users;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
@ToString
@Getter
public class CommentSaveRequest {

    @NotNull
    @Positive
    private Long referencePostId;
    @NotNull
    private String commentContent;
    @NotNull
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
