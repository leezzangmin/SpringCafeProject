package com.zzangmin.gesipan.layer.basiccrud.dto.comment;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Getter
public class CommentUpdateRequest {

    @NotNull
    private String commentContent;
    @NotNull
    @PastOrPresent
    private LocalDateTime updatedAt;

}
