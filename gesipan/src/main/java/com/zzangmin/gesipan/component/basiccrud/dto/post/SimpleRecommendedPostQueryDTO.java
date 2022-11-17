package com.zzangmin.gesipan.component.basiccrud.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SimpleRecommendedPostQueryDTO {
    private final Long postId;
    private final String postSubject;
    private final LocalDateTime createdAt;
    private final Long hitCount;
    private final Long userId;
    private final String userNickname;
}
