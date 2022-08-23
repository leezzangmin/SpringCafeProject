package com.zzangmin.gesipan.web.dto.post;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
public class PostRecommendRequest {
    @NotNull
    private long postId;
    @NotNull
    private long userId;
}
