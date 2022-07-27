package com.zzangmin.gesipan.web.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String message;
    private int status;
    private String code;
    private List<String> errors = new ArrayList<>();

    public static ErrorResponse of(ErrorCode errorCode) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.message = errorCode.getMessage();
        errorResponse.status = errorCode.getStatus();
        errorResponse.code = errorCode.getCode();
        return errorResponse;
    }

    public static ErrorResponse of(ErrorCode errorCode, String errorMessage) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.message = errorMessage;
        errorResponse.status = errorCode.getStatus();
        errorResponse.code = errorCode.getCode();
        return errorResponse;
    }

    public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.message = errorCode.getMessage();
        errorResponse.status = errorCode.getStatus();
        errorResponse.code = errorCode.getCode();
        errorResponse.errors = bindingResult.getAllErrors()
                .stream()
                .map(i -> i.toString())
                .collect(Collectors.toList());
        return errorResponse;
    }
}