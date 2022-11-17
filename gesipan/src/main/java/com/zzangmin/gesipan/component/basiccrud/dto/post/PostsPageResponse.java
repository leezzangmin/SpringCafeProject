package com.zzangmin.gesipan.component.basiccrud.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ToString
@Getter
@AllArgsConstructor
public class PostsPageResponse {
    private final Long categoryId;
    private final List<PostPageResponse> postPageResponseList;

    @ToString
    @Getter
    @Builder
    public static class PostPageResponse {
        private final Long postId;
        private final String postSubject;
        private final LocalDateTime createdAt;
        private final Long hitCount;
        private final int recommendCount;
        private final Long userId;
        private final String userNickname;
        private final int commentCount;

        private static PostPageResponse of(PostSimpleQueryDTO post, int recommendCount, int commentCount) {
            return PostPageResponse.builder()
                    .postId(post.getPostId())
                    .postSubject(post.getPostSubject())
                    .createdAt(post.getCreatedAt())
                    .hitCount(post.getHitCount())
                    .recommendCount(recommendCount)
                    .userId(post.getUserId())
                    .userNickname(post.getUserNickname())
                    .commentCount(commentCount)
                    .build();
        }
    }

    public static PostsPageResponse of(Long categoryId, List<PostSimpleQueryDTO> postsDTO, List<Integer> recommendCount, List<Integer> commentCounts) {
        return new PostsPageResponse(categoryId, IntStream.range(0, postsDTO.size()).boxed()
                .map(i ->  PostPageResponse.of(postsDTO.get(i), recommendCount.get(i), commentCounts.get(i)))
                .collect(Collectors.toList()));
    }

}
