package com.zzangmin.gesipan.layer.basiccrud.dto.comment;

import com.zzangmin.gesipan.layer.basiccrud.entity.Comment;
import com.zzangmin.gesipan.layer.embeddable.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private Long commentId;
    private Long userId;
    private String userNickname;
    private String content;
    private BaseTime baseTime;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;

    public static CommentResponse of(Comment comment) {
        return new CommentResponse(comment.getCommentId(), comment.getUser().getUserId(), comment.getUser().getUserName(), comment.getCommentContent(), comment.getBaseTime());
    }
}

