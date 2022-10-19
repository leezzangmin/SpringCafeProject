package com.zzangmin.gesipan.layer.basiccrud.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequest {
    @NotBlank
    @Length(max = 1000)
    private String postSubject;
    @NotBlank
    private String postContent;
    @NotNull
    @PastOrPresent
    private LocalDateTime updatedAt;
}
