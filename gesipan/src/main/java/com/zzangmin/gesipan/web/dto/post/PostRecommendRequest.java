package com.zzangmin.gesipan.web.dto.post;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PostRecommendRequest {
    @NotBlank
    private long postId;
    @NotBlank
    private long userId;
}
