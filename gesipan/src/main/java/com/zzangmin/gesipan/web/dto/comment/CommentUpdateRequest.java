package com.zzangmin.gesipan.web.dto.comment;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Getter
public class CommentUpdateRequest {

    @NotBlank
    private String commentContent;
    @NotBlank
    @PastOrPresent
    private LocalDateTime updatedAt;

}
