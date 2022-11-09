package com.zzangmin.gesipan.layer.basiccrud.dto.comment;


import com.zzangmin.gesipan.layer.basiccrud.entity.Comment;
import com.zzangmin.gesipan.layer.login.entity.Users;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PersonalCommentsResponse {

    private final Long userId;
    private final String userNickname;
    private final List<SingleCommentResponse> singleCommentResponses;

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SingleCommentResponse {
        private final Long referencePostId;
        private final Long commentId;
        private final String commentContent;
        private final LocalDateTime createdAt;
    }

    public static PersonalCommentsResponse of(Users user, List<Comment> comments) {
        return new PersonalCommentsResponse(user.getUserId(), user.getUserNickname(),
                comments.stream()
                        .map(comment -> SingleCommentResponse.builder()
                                        .referencePostId(comment.getPost().getPostId())
                                        .commentId(comment.getCommentId())
                                        .commentContent(comment.getCommentContent())
                                        .createdAt(comment.getCreatedAt())
                                        .build())
                        .collect(Collectors.toList())
        );
    }

}
