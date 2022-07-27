package com.zzangmin.gesipan.web.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@ToString
@Getter
@AllArgsConstructor
public class PostUpdateRequest {
    @NotBlank
    @Length(max = 1000)
    private String postSubject;
    @NotBlank
    private String postContent;
    @NotBlank
    @PastOrPresent
    private LocalDateTime updatedAt;
}
