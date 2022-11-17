package com.zzangmin.gesipan.component.basiccrud.dto.post;

import com.zzangmin.gesipan.component.login.entity.Users;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 특정 유저만의 게시물 목록을 보여주는 DTO
 */

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PersonalPostsResponse {

    private final Long userId;
    private final String userNickname;
    private final List<PersonalPostResponse> posts;

    @Getter
    @Builder
    public static class PersonalPostResponse {
        private final Long postId;
        private final String postSubject;
        private final LocalDateTime createdAt;
        private final Long hitCount;
        private final int recommendCount;
        private final int commentCount;
    }

    public static PersonalPostsResponse of(Users user, List<SimpleUsersPostQueryDTO> posts, List<Integer> recommendCount, List<Integer> commentCount) {
        return new PersonalPostsResponse(user.getUserId(), user.getUserNickname(),
                IntStream.range(0, posts.size()).boxed()
                        .map(i -> PersonalPostResponse
                        .builder()
                        .postId(posts.get(i).getPostId())
                        .postSubject(posts.get(i).getPostSubject())
                        .createdAt(posts.get(i).getCreatedAt())
                        .hitCount(posts.get(i).getHitCount())
                        .recommendCount(recommendCount.get(i))
                        .commentCount(commentCount.get(i))
                        .build())
                        .collect(Collectors.toList())
        );
    }

}
