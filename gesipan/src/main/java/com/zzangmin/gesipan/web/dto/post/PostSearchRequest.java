package com.zzangmin.gesipan.web.dto.post;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class PostSearchRequest {
    private String userNickname;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Long postCategoryId;
}
