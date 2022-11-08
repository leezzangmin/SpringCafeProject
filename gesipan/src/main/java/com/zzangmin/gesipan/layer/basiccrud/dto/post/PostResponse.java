package com.zzangmin.gesipan.layer.basiccrud.dto.post;

import com.zzangmin.gesipan.layer.basiccrud.dto.comment.CommentResponse;
import com.zzangmin.gesipan.layer.basiccrud.entity.Comment;
import com.zzangmin.gesipan.layer.basiccrud.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PostResponse {

    private final Long postId;
    private final Long userId;
    private final String userNickname;
    private final Long hitCount;
    private final int recommendCount;
    private final String postSubject;
    private final String postContent;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<CommentResponse> comments;
    private final boolean isRecommended;

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
