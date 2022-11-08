package com.zzangmin.gesipan.layer.basiccrud.dto.temporarypost;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor
public class TemporaryPostSaveRequest {
    @NotBlank
    private final String postSubject;
    @NotBlank
    private final String postContent;
    @NotNull
    private final Long postCategoryId;
}
