package com.zzangmin.gesipan.component.basiccrud.dto.comment;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private final Long commentId;
    private final String commentContent;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Long userId;
    private final String userNickname;

}

