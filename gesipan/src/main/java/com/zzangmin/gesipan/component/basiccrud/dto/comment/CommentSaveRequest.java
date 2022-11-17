package com.zzangmin.gesipan.component.basiccrud.dto.comment;

import com.zzangmin.gesipan.component.basiccrud.entity.Comment;
import com.zzangmin.gesipan.component.basiccrud.entity.Post;
import com.zzangmin.gesipan.component.embeddable.BaseTime;
import com.zzangmin.gesipan.component.login.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
@ToString
@Getter
@AllArgsConstructor
public class CommentSaveRequest {

    @NotNull
    @Positive
    private final Long referencePostId;
    @NotNull
    private final String commentContent;
    @NotNull
    @PastOrPresent
    private final LocalDateTime createdAt;

    public Comment toEntity(Users user, Post post) {
        return Comment.builder()
                .commentContent(this.commentContent)
                .baseTime(new BaseTime(this.createdAt, this.createdAt))
                .user(user)
                .post(post)
                .build();
    }
}
