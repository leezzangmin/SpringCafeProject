package com.zzangmin.gesipan.layer.basiccrud.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostSimpleQueryDTO {

    private final Long postId;
    private final String postSubject;
    private final LocalDateTime createdAt;
    private final Long hitCount;

    private final Long userId;
    private final String userNickname;

}
