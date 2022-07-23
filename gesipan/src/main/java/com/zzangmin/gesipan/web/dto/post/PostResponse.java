package com.zzangmin.gesipan.web.dto.post;

import com.zzangmin.gesipan.web.entity.Comment;
import com.zzangmin.gesipan.web.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class PostResponse {

    private Long postId;
    private Long userId;
    private String userNickname;
    private Long hitCount;
    private int recommendCount;
    private String subject;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentResponse> comments;

    @Getter
    @AllArgsConstructor
    private static class CommentResponse {
        private Long commentId;
        private Long userId;
        private String userNickname;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private static CommentResponse of(Comment comment) {
            return new CommentResponse(comment.getCommentId(), comment.getUser().getUserId(), comment.getUser().getUserName(), comment.getCommentContent(), comment.getCreatedAt(), comment.getUpdatedAt());
        }
    }

    public static PostResponse of(Post post, List<Comment> comments) {
        return PostResponse.builder()
                .postId(post.getPostId())
                .userId(post.getUser().getUserId())
                .userNickname(post.getUser().getUserNickname())
                .hitCount(post.getHitCount())
                .recommendCount(0)
                .subject(post.getPostSubject())
                .content(post.getPostContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .comments(comments.stream().map(i -> CommentResponse.of(i)).collect(Collectors.toList()))
                .build();
        // return new PostResponse(post.getPostId(), post.getUser().getUserId(), post.getUser().getUserNickname(), post.getHitCount(), post.getPostSubject(), post.getPostContent(), post.getCreatedAt(), post.getUpdatedAt(), comments.stream().map(i -> CommentResponse.of(i)).collect(Collectors.toList()));
    }


}
