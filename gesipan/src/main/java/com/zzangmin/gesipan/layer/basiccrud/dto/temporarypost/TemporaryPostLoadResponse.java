package com.zzangmin.gesipan.layer.basiccrud.dto.temporarypost;

import com.zzangmin.gesipan.layer.basiccrud.entity.TemporaryPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@AllArgsConstructor
public class TemporaryPostLoadResponse {
    private Long userId;
    private List<TemporaryPostLoadResponses> savedPosts;

    @Getter
    @Builder
    private static class TemporaryPostLoadResponses {
        private String postSubject;
        private String postContent;
        private LocalDateTime createdAt;
    }

    public static TemporaryPostLoadResponse of(Long userId, List<TemporaryPost> temporaryPosts) {
        return new TemporaryPostLoadResponse(userId,
                temporaryPosts.stream()
                        .map(i -> TemporaryPostLoadResponses.builder()
                        .postSubject(i.getPostSubject())
                        .postContent(i.getPostContent())
                        .createdAt(i.getCreatedAt())
                        .build())
                        .collect(Collectors.toList())

        );
    }
}
