package com.zzangmin.gesipan.component.basiccrud.dto.post;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class PostSearchRequest {
    private final String userNickname;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    @NotNull
    private final Long postCategoryId;
}
