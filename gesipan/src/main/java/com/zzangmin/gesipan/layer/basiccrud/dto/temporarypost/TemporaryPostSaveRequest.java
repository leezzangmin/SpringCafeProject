package com.zzangmin.gesipan.layer.basiccrud.dto.temporarypost;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
public class TemporaryPostSaveRequest {
    @NotBlank
    private String postSubject;
    @NotBlank
    private String postContent;
    @NotNull
    private Long postCategoryId;
}
