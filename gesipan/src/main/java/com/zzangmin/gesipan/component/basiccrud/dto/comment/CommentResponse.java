package com.zzangmin.gesipan.component.basiccrud.dto.comment;

import com.zzangmin.gesipan.component.basiccrud.entity.Comment;
import com.zzangmin.gesipan.component.embeddable.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private final Long commentId;
    private final Long userId;
    private final String userNickname;
    private final String content;
    private final BaseTime baseTime;

    public static CommentResponse of(Comment comment) {
        return new CommentResponse(comment.getCommentId(), comment.getUser().getUserId(), comment.getUser().getUserName(), comment.getCommentContent(), comment.getBaseTime());
    }
}

