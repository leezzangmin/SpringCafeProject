package com.zzangmin.gesipan.web.dto;

import com.zzangmin.gesipan.web.entity.Comment;
import com.zzangmin.gesipan.web.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class PostResponse {

    private Long postId;
    private Long userId;
    private String userNickname;
    private int recommendCount;
    private Long hitCount;
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
        return new PostResponse(post.getPostId(), post.getUser().getUserId(), post.getUser().getUserNickname(), post.getRecommendCount(), post.getHitCount(), post.getPostSubject(), post.getPostContent(), post.getCreatedAt(), post.getUpdatedAt(), comments.stream().map(i -> CommentResponse.of(i)).collect(Collectors.toList()));
    }


}
