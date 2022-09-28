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

/**
 * 요청한 유저가 추천한 게시물 목록 DTO
 *
 */

@Getter
@AllArgsConstructor
public class PostRecommendsResponse {

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


    public static PostRecommendsResponse of(List<Post> postRecommends, List<Integer> recommendCount, List<Integer> commentCount) {
        return new PostRecommendsResponse(
            IntStream.range(0, postRecommends.size()).boxed()
                .map(index -> SinglePost.builder()
                    .postId(postRecommends.get(index).getPostId())
                    .postContent(postRecommends.get(index).getPostContent())
                    .createdAt(postRecommends.get(index).getCreatedAt())
                    .hitCount(postRecommends.get(index).getHitCount())
                    .userId(postRecommends.get(index).getUser().getUserId())
                    .userNickname(postRecommends.get(index).getUser().getUserNickname())
                    .recommendCount(recommendCount.get(index))
                    .commentCount(commentCount.get(index))
                    .build())
                .collect(Collectors.toList()), postRecommends.size());
    }

}
