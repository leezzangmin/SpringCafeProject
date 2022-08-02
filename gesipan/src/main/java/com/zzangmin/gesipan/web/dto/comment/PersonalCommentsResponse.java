package com.zzangmin.gesipan.web.dto.comment;


import com.zzangmin.gesipan.web.entity.Comment;
import com.zzangmin.gesipan.web.entity.Users;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PersonalCommentsResponse {

    private Long userId;
    private String userNickname;
    private List<PersonalCommentResponse> personalCommentResponses = new ArrayList<>();

    @Getter
    @Builder
    static class PersonalCommentResponse {
        private Long referencePostId;
        private Long commentId;
        private String commentContent;
        private LocalDateTime createdAt;
    }

    public static PersonalCommentsResponse of(Users user, List<Comment> comments) {
        return new PersonalCommentsResponse(user.getUserId(), user.getUserNickname(),
                comments.stream()
                        .map(i -> PersonalCommentResponse.builder()
                                        .referencePostId(i.getPost().getPostId())
                                        .commentId(i.getCommentId())
                                        .commentContent(i.getCommentContent())
                                        .createdAt(i.getCreatedAt())
                                        .build())
                        .collect(Collectors.toList())
        );
    }

}
