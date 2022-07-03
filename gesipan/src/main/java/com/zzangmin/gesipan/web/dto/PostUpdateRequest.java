package com.zzangmin.gesipan.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@AllArgsConstructor
public class PostUpdateRequest {
    private String postSubject;
    private String postContent;
    private LocalDateTime updatedAt;
}
