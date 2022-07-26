package com.zzangmin.gesipan.web.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;


@ToString
@Getter
@NoArgsConstructor
public class PostSaveRequest {

    @NotBlank
    @Length(max = 1000)
    private String postSubject;
    @NotBlank
    private String postContent;
    @NotBlank @Positive
    private Long userId;
    @NotBlank @Positive
    private Long postCategoryId;
    @NotBlank
    @PastOrPresent
    private LocalDateTime createdAt;

}
