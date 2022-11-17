package com.zzangmin.gesipan.component.basiccrud.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@AllArgsConstructor
public class PostRecommendRequest {
    @NotNull
    private final long postId;
    @NotNull
    private final long userId;
}
