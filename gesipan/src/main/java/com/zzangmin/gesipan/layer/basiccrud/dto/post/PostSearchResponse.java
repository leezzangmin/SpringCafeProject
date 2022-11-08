package com.zzangmin.gesipan.layer.basiccrud.dto.post;

import com.zzangmin.gesipan.layer.basiccrud.entity.Post;
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

    private final List<SinglePost> posts;
    private final int searchCount;

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PACKAGE)
    private static class SinglePost {
        private final Long postId;
        private final String postContent;
        private final LocalDateTime createdAt;
        private final Long hitCount;
        private final Long userId;
        private final String userNickname;
        private final int recommendCount;
        private final int commentCount;
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
