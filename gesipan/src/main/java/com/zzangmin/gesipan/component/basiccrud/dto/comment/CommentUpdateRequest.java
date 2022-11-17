package com.zzangmin.gesipan.component.basiccrud.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentUpdateRequest {

    @NotNull
    private final String commentContent;
    @NotNull
    @PastOrPresent
    private final LocalDateTime updatedAt;

}
