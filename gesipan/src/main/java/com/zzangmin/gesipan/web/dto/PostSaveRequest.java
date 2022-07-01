package com.zzangmin.gesipan.web.dto;

import com.zzangmin.gesipan.web.entity.Categories;
import com.zzangmin.gesipan.web.entity.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;


@ToString
@Getter
@RequiredArgsConstructor
public class PostSaveRequest {

    @Length(max = 1000) @NotNull
    private final String postSubject;
    @NotNull
    private final String postContent;
    @NotNull @Positive
    private final Long userId;
    @NotNull
    private final Long postCategoryId;
    @NotNull
    private final LocalDateTime createdAt;

}
