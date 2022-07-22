package com.zzangmin.gesipan.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@ToString
@Getter
@AllArgsConstructor
public class PostUpdateRequest {
    @NotNull
    @Length(max = 1000)
    private String postSubject;
    @NotNull
    private String postContent;
    @NotNull
    @PastOrPresent
    private LocalDateTime updatedAt;
}
