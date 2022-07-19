package com.zzangmin.gesipan.web.dto;

import com.zzangmin.gesipan.web.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

        public static PostPageResponse of(Post post) {
            return PostPageResponse.builder()
                    .postId(post.getPostId())
                    .postSubject(post.getPostSubject())
                    .createdAt(post.getCreatedAt())
                    .hitCount(post.getHitCount())
                    .userId(post.getUser().getUserId())
                    .userNickname(post.getUser().getUserNickname())
                    .build();
        }
    }

    private PostsPageResponse(Long categoryId, List<PostPageResponse> postPageResponses) {
        this.categoryId = categoryId;
        this.postPageResponseList = postPageResponses;
    }

    public static PostsPageResponse of(Long categoryId, List<Post> posts) {
        return new PostsPageResponse(categoryId, posts.stream()
                .map(i -> PostPageResponse.of(i))
                .collect(Collectors.toList()));
    }


}
