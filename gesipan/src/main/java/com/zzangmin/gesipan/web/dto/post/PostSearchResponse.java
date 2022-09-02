package com.zzangmin.gesipan.web.dto.post;

import com.zzangmin.gesipan.web.entity.Post;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostSearchResponse {

    private List<SinglePost> posts;
    private int searchCount;

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PACKAGE)
    private static class SinglePost {
        private Long postId;
        private String postContent;
        private LocalDateTime createdAt;
        private Long hitCount;
        private Long userId;
        private String userNickname;
        private int recommendCount;
        private int commentCount;
    }

    public static PostSearchResponse of(List<Post> posts, List<Integer> recommendCount, List<Integer> commentsCount) {
        return new PostSearchResponse(IntStream.range(0, posts.size()).boxed()
            .map(index -> SinglePost.builder()
                .postId(posts.get(index).getPostId())
                .postContent(posts.get(index).getPostContent())
                .createdAt(posts.get(index).getCreatedAt())
                .hitCount(posts.get(index).getHitCount())
                .userId(posts.get(index).getUser().getUserId())
                .userNickname(posts.get(index).getUser().getUserNickname())
                .recommendCount(recommendCount.get(index))
                .commentCount(commentsCount.get(index))
                .build())
            .collect(Collectors.toList()),
            posts.size());
    }

}
