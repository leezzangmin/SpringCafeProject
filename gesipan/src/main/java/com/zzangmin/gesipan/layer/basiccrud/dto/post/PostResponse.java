package com.zzangmin.gesipan.layer.basiccrud.dto.post;

import com.zzangmin.gesipan.layer.basiccrud.dto.comment.CommentResponse;
import com.zzangmin.gesipan.layer.basiccrud.entity.Comment;
import com.zzangmin.gesipan.layer.basiccrud.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
