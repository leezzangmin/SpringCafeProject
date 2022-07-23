package com.zzangmin.gesipan.web.dto.post;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PostRecommendRequest {
    @NotNull
    private long postId;
    @NotNull
    private long userId;
}
