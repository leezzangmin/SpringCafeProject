package com.zzangmin.gesipan.component.basiccrud.dto.post;

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

    private final List<SinglePost> posts;
    private final int searchCount;

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PACKAGE)
    public static class SinglePost {
        private final Long postId;
        private final String postSubject;
        private final LocalDateTime createdAt;
        private final Long hitCount;
        private final Long userId;
        private final String userNickname;
        private final int recommendCount;
        private final int commentCount;
    }


    public static PostRecommendsResponse of(List<SimpleRecommendedPostQueryDTO> postRecommends, List<Integer> recommendCount, List<Integer> commentCount) {
        return new PostRecommendsResponse(
            IntStream.range(0, postRecommends.size()).boxed()
                .map(index -> SinglePost.builder()
                    .postId(postRecommends.get(index).getPostId())
                    .postSubject(postRecommends.get(index).getPostSubject())
                    .createdAt(postRecommends.get(index).getCreatedAt())
                    .hitCount(postRecommends.get(index).getHitCount())
                    .userId(postRecommends.get(index).getUserId())
                    .userNickname(postRecommends.get(index).getUserNickname())
                    .recommendCount(recommendCount.get(index))
                    .commentCount(commentCount.get(index))
                    .build())
                .collect(Collectors.toList()), postRecommends.size());
    }

}
