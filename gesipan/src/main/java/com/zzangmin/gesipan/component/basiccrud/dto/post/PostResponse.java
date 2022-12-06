package com.zzangmin.gesipan.component.basiccrud.dto.post;

import com.zzangmin.gesipan.component.basiccrud.dto.comment.CommentResponse;
import com.zzangmin.gesipan.component.basiccrud.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private Long postId;
    private Long userId;
    private String userNickname;
    private Long hitCount;
    private int recommendCount;
    private String postSubject;
    private String postContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentResponse> comments;
    private boolean isRecommended;

    public static PostResponse of(Post post, List<CommentResponse> commentResponses, int recommendCount, boolean isRecommendedFlag) {
        return PostResponse.builder()
                .postId(post.getPostId())
                .userId(post.getUser().getUserId())
                .userNickname(post.getUser().getUserNickname())
                .hitCount(post.getHitCount())
                .recommendCount(recommendCount)
                .postSubject(post.getPostSubject())
                .postContent(post.getPostContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .isRecommended(isRecommendedFlag)
                .comments(commentResponses)
                .build();
    }


}
