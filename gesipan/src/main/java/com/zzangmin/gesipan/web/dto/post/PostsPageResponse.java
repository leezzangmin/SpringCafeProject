package com.zzangmin.gesipan.web.dto.post;

import com.zzangmin.gesipan.web.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class PostsPageResponse {
    private Long categoryId;
    private List<PostPageResponse> postPageResponseList;

    @Getter
    @Builder
    private static class PostPageResponse {
        private Long postId;
        private String postSubject;
        private LocalDateTime createdAt;
        private Long hitCount;
        private int recommendCount;

        private Long userId;
        private String userNickname;

        private int commentCount;

        private static PostPageResponse of(Post post, int recommendCount, int commentCount) {
            return PostPageResponse.builder()
                    .postId(post.getPostId())
                    .postSubject(post.getPostSubject())
                    .createdAt(post.getCreatedAt())
                    .hitCount(post.getHitCount())
                    .recommendCount(recommendCount)
                    .userId(post.getUser().getUserId())
                    .userNickname(post.getUser().getUserNickname())
                    .commentCount(commentCount)
                    .build();
        }
    }

    private PostsPageResponse(Long categoryId, List<PostPageResponse> postPageResponses) {
        this.categoryId = categoryId;
        this.postPageResponseList = postPageResponses;
    }

    public static PostsPageResponse of(Long categoryId, List<Post> posts, List<Integer> recommendCount, List<Integer> commentCounts) {
        return new PostsPageResponse(categoryId, IntStream.range(0, posts.size()).boxed()
                .map(i ->  PostPageResponse.of(posts.get(i), recommendCount.get(i), commentCounts.get(i)))
                .collect(Collectors.toList()));
    }


}
