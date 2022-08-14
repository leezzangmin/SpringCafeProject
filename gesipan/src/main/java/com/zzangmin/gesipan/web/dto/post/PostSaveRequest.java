package com.zzangmin.gesipan.web.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;


@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostSaveRequest {

    @NotBlank
    @Length(max = 1000)
    private String postSubject;
    @NotBlank
    private String postContent;
    @NotNull
    @Positive
    private Long userId;
    @NotNull @Positive
    private Long postCategoryId;
    @NotNull
    @PastOrPresent
    private LocalDateTime createdAt;

}
