package com.zzangmin.gesipan.component.basiccrud.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@ToString
@Getter
@AllArgsConstructor
public class PostUpdateRequest {
    @NotBlank
    @Length(max = 1000)
    private final String postSubject;
    @NotBlank
    private final String postContent;
    @NotNull
    @PastOrPresent
    private final LocalDateTime updatedAt;
}
